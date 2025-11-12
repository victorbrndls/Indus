package com.victorbrndls.indus.gui;

import com.victorbrndls.indus.blocks.tileentity.Container1BlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class Container1Menu extends AbstractContainerMenu {

    public final Container1BlockEntity entity;

    public Container1Menu(int id, Inventory playerInventory, Container1BlockEntity entity) {
        super(IndusMenus.CONTAINER_1.get(), id);
        this.entity = entity;

        addSlot(new Slot(entity.getContainer(), 0, 80, 38));

        addStandardInventorySlots(playerInventory, 8, 88);
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
