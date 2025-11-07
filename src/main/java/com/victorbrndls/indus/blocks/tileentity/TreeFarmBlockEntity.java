package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.gui.BaseStructureMenu;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TreeFarmBlockEntity extends BaseStructureBlockEntity {

    public TreeFarmBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.TREE_FARM_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.TREE_FARM;
    }

    @Override
    protected Item getResource() {
        return Items.OAK_LOG;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BaseStructureMenu(id, inventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.tree_farm");
    }
}