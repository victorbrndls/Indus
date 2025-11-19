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

        if (playerInventory != null) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 88 + i * 18));
                }
            }

            for (int k = 0; k < 9; k++) {
                addSlot(new Slot(playerInventory, k, 8 + k * 18, 58 + 88));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack tmpStack = slot.getItem();
            itemstack = tmpStack.copy();
            if (index == 0) {
                if (!this.moveItemStackTo(tmpStack, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(tmpStack, 0, 1, false)) {
                if (index < 28) {
                    if (!this.moveItemStackTo(tmpStack, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 37) {
                    if (!this.moveItemStackTo(tmpStack, 1, 28, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (tmpStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (tmpStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, tmpStack);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(
                ContainerLevelAccess.create(entity.getLevel(), entity.getBlockPos()),
                player,
                entity.getBlockState().getBlock());
    }

}
