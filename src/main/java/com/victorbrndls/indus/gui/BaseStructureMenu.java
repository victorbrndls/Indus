package com.victorbrndls.indus.gui;

import com.victorbrndls.indus.blocks.tileentity.BaseStructureBlockEntity;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.mod.structure.IndusStructureRequirements;
import com.victorbrndls.indus.mod.structure.IndusStructureState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class BaseStructureMenu extends AbstractContainerMenu {

    public final BaseStructureBlockEntity entity;
    private final List<ItemStack> requirements;
    private final SimpleContainerData data;

    public static final int BUTTON_BUILD = 0;

    public BaseStructureMenu(int id, Inventory playerInventory, BaseStructureBlockEntity entity) {
        super(IndusMenus.BASE_STRUCTURE.get(), id);
        this.entity = entity;
        this.requirements = IndusStructureRequirements.getRequirements(entity.getStructureType());
        this.data = new SimpleContainerData(requirements.size());
        addDataSlots(data);

        if (playerInventory != null) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + 116));
                }
            }

            for (int k = 0; k < 9; k++) {
                addSlot(new Slot(playerInventory, k, 8 + k * 18, 142 + 116));
            }
        }
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

    @Override
    public void broadcastChanges() {
        updateHaveCounts();
        super.broadcastChanges();
    }

    private void updateHaveCounts() {
        for (int i = 0; i < data.getCount(); i++) {
            data.set(i, 0);
        }

        BlockPos inputPos = entity.inputPos();
        BlockEntity be = entity.getLevel().getBlockEntity(inputPos);
        if (be == null) return;

        // Vanilla-compatible path
        if (be instanceof Container c) {
            for (int i = 0; i < c.getContainerSize(); i++) {
                ItemStack s = c.getItem(i);
                if (s.isEmpty()) continue;
                for (int r = 0; r < requirements.size(); r++) {
                    if (ItemStack.isSameItemSameComponents(s, requirements.get(r))) {
                        data.set(r, data.get(r) + s.getCount());
                    }
                }
            }
        } else {
            throw new UnsupportedOperationException();

            // NeoForge transfer API path (optional)
//            var handler = entity.getLevel().getCapability(Capabilities.Item.BLOCK, inputPos, null);
//            if (handler != null) {
//                try (var ignored = Transaction.open(null)) {
//                    for (int r = 0; r < requirements.size(); r++) {
//                        var res = ItemResource.of(requirements.get(r));
//                        have[r] = 1; // handler.getAmountAsInt(r);
//                    }
//                }
//            }
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == BUTTON_BUILD) {
            if (!(player instanceof ServerPlayer)) return true;
            entity.startBuilding(false);
            return true;
        }
        return false;
    }

    public List<ItemStack> requirements() {
        return requirements;
    }

    public IndusStructureState getStructureState() {
        return entity.getState();
    }

    public boolean canBuildClient() {
        var requirements = requirements();

        for (int i = 0; i < requirements.size(); i++) {
            if (data.get(i) < requirements.get(i).getCount()) {
                return false;
            }
        }

        return true;
    }

    public int getHave(int index) {
        return data.get(index);
    }
}
