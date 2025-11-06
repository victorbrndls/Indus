package com.victorbrndls.indus.blocks.tileentity;

import com.mojang.serialization.Codec;
import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.shared.BlockHelper;
import com.victorbrndls.indus.shared.OreLocator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

public class QuarryBlockEntity extends BlockEntity {

    private int tickCounter = 0;

    @Nullable
    private Item prospectedOre = null;
    private boolean hasProspected = false;

    public QuarryBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.QUARRY_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }

        if (!hasProspected) {
            prospectedOre = OreLocator.prospect(level, pos);
            hasProspected = true;
        }

        if (prospectedOre == null) return;

        tickCounter++;
        if (tickCounter < 100) return;
        tickCounter = 0;

        Item result;
        if (prospectedOre.equals(Items.IRON_ORE)) {
            result = Items.RAW_IRON;
        } else if (prospectedOre.equals(Items.COPPER_ORE)) {
            result = Items.RAW_COPPER;
        } else if (prospectedOre.equals(Items.COAL_ORE)) {
            result = Items.COAL;
        } else if (prospectedOre.equals(Items.GOLD_ORE)) {
            result = Items.RAW_GOLD;
        } else {
            Indus.LOGGER.warn("Quarry found unsupported ore type: {}", prospectedOre);
            return;
        }

        BlockPos target = BlockHelper.offsetFrontFacing(pos, state, 7, 0, 1);
        if (!level.isLoaded(target)) return;

        BlockState ts = level.getBlockState(target);
        BlockEntity te = level.getBlockEntity(target);
        if (te == null) return;

        ResourceHandler<ItemResource> handler = level.getCapability(Capabilities.Item.BLOCK, target, ts, te, null);

        if (handler != null) {
            try (Transaction tx = Transaction.open(null)) {
                long inserted = handler.insert(ItemResource.of(result), 1, tx);
                if (inserted == 1) tx.commit();
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        output.store("hasProspected", Codec.BOOL, hasProspected);
        if (prospectedOre != null) {
            output.store("prospectedOre", Codec.STRING, BuiltInRegistries.ITEM.getKey(prospectedOre).toString());
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        hasProspected = input.read("hasProspected", Codec.BOOL).orElse(false);

        String oreId = input.read("prospectedOre", Codec.STRING).orElse(null);
        if (oreId != null) {
            ResourceLocation id = ResourceLocation.tryParse(oreId);
            if (id == null) return;
            BuiltInRegistries.ITEM.getOptional(id).ifPresent((ore) -> this.prospectedOre = ore);
        }
    }

}