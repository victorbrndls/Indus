package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.gui.BaseStructureMenu;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.shared.BlockHelper;
import com.victorbrndls.indus.shared.OreLocator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

public class QuarryBlockEntity extends BaseStructureBlockEntity {

    @Nullable
    private Item prospectedOre = null;
    private boolean hasProspected = false;

    public QuarryBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.QUARRY_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.QUARRY;
    }

    private Item getResource() {
        if (!hasProspected) {
            prospectedOre = OreLocator.prospect(level, getBlockPos());
            hasProspected = true;
        }

        if (prospectedOre == null) return null;

        if (prospectedOre.equals(Items.STONE)) {
            return Items.COBBLESTONE;
        } else if (prospectedOre.equals(Items.IRON_ORE)) {
            return Items.RAW_IRON;
        } else if (prospectedOre.equals(Items.COPPER_ORE)) {
            return Items.RAW_COPPER;
        } else if (prospectedOre.equals(Items.COAL_ORE)) {
            return Items.COAL;
        } else if (prospectedOre.equals(Items.GOLD_ORE)) {
            return Items.RAW_GOLD;
        } else {
            Indus.LOGGER.warn("Quarry found unsupported ore type: {}", prospectedOre);
            return null;
        }
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        tickCounter++;

        if (tickCounter >= 100) {
            tickCounter = 0;

            BlockPos target = BlockHelper.offsetFrontFacing(pos, state, 7, 0, 1);
            ResourceHandler<ItemResource> handler = BlockHelper.getItemHandlerAt(level, target);
            if (handler == null) return;

            try (Transaction tx = Transaction.open(null)) {
                var resource = getResource();
                if (resource != null) {
                    long inserted = handler.insert(ItemResource.of(resource), 1, tx);
                    if (inserted == 1) tx.commit();
                }
            }
        }
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BaseStructureMenu(id, inventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.quarry");
    }
}