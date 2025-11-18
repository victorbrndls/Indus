package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.shared.OreLocator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

public class QuarryBlockEntity extends BaseStructureBlockEntity {

    private static final BlockPos OUTPUT_POS = new BlockPos(7, 1, 0);

    @Nullable
    private Item prospectedOre = null;
    private boolean hasProspected = false;

    public QuarryBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.QUARRY.get(), pos, state);
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
        if ((level.getGameTime() % 80) != 0) return;

        var handler = getRelativeItemHandler(level, OUTPUT_POS);
        if (handler == null) return;

        var resource = getResource();
        if (resource == null) return;

        ItemHandlerHelper.insertItem(handler, new ItemStack(resource, 1), false);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.quarry");
    }
}