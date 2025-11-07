package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.gui.BaseStructureMenu;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.shared.OreLocator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
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

    @Override
    protected Item getResource() {
        if (!hasProspected) {
            prospectedOre = OreLocator.prospect(level, getBlockPos());
            hasProspected = true;
        }

        if (prospectedOre == null) return null;

        if (prospectedOre.equals(Items.IRON_ORE)) {
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
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BaseStructureMenu(id, inventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.quarry");
    }
}