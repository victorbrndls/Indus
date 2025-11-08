package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.gui.BaseStructureMenu;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlastFurnaceBlockEntity extends BaseStructureBlockEntity {

    public BlastFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.BLAST_FURNACE_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.BLAST_FURNACE;
    }

    @Override
    protected Item getResource() {
        return null;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BaseStructureMenu(id, inventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.blast_furnace");
    }
}