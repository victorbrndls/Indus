package com.victorbrndls.indus.inventory;

import com.victorbrndls.indus.blocks.tileentity.TreeFarmBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class TreeFarmMenu extends AbstractContainerMenu {

    public final TreeFarmBlockEntity entity;

    public TreeFarmMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, null);
    }

    public TreeFarmMenu(int id, Inventory playerInventory, TreeFarmBlockEntity entity) {
        super(IndusMenus.TREE_FARM.get(), id);
        this.entity = entity;
        this.addStandardInventorySlots(playerInventory, 8, 116);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(
                ContainerLevelAccess.create(entity.getLevel(), entity.getBlockPos()),
                player,
                entity.getBlockState().getBlock()
        );
    }
}
