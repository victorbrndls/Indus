package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.blocks.CartUnloaderBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.MinecartHopper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UnloaderBlockEntity extends BlockEntity implements MenuProvider {

    private final SimpleContainer inv = new SimpleContainer(9) {
        @Override
        public void setChanged() {
            UnloaderBlockEntity.this.setChanged();
        }
    };

    public UnloaderBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.CART_UNLOADER.get(), pos, state);
    }

    public SimpleContainer getContainer() {
        return inv;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, UnloaderBlockEntity blockEntity) {
        if (level.isClientSide) return;

        // Find Hopper Minecart above
        List<MinecartHopper> minecarts = level.getEntitiesOfClass(MinecartHopper.class, new AABB(pos.above()));
        if (minecarts.isEmpty()) {
            if (state.getValue(CartUnloaderBlock.POWERED)) {
                level.setBlock(pos, state.setValue(CartUnloaderBlock.POWERED, false), 3);
            }
            return;
        }

        MinecartHopper minecart = minecarts.get(0);
        boolean isMinecartEmpty = minecart.isEmpty();

        if (!isMinecartEmpty) {
            // Try to move items from minecart to this inventory
            moveItems(minecart, blockEntity.inv);
            // Re-check emptiness after move
            isMinecartEmpty = minecart.isEmpty();
        }

        boolean shouldBePowered = isMinecartEmpty;
        if (state.getValue(CartUnloaderBlock.POWERED) != shouldBePowered) {
            level.setBlock(pos, state.setValue(CartUnloaderBlock.POWERED, shouldBePowered), 3);
        }
    }

    private static void moveItems(MinecartHopper source, SimpleContainer target) {
        for (int i = 0; i < source.getContainerSize(); i++) {
            ItemStack sourceStack = source.getItem(i);
            if (sourceStack.isEmpty()) continue;

            ItemStack remaining = addItem(target, sourceStack);
            if (remaining.getCount() != sourceStack.getCount()) {
                source.setItem(i, remaining);
                target.setChanged();
                source.setChanged();
            }
        }
    }

    // Helper to add item to container, similar to ContainerHelper or ItemHandlerHelper but for SimpleContainer
    private static ItemStack addItem(SimpleContainer container, ItemStack stack) {
        ItemStack toAdd = stack.copy();
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (toAdd.isEmpty()) break;
            ItemStack slotStack = container.getItem(i);
            
            if (slotStack.isEmpty()) {
                container.setItem(i, toAdd.copy());
                toAdd.setCount(0);
            } else if (ItemStack.isSameItemSameComponents(slotStack, toAdd)) {
                int available = slotStack.getMaxStackSize() - slotStack.getCount();
                int move = Math.min(available, toAdd.getCount());
                slotStack.grow(move);
                toAdd.shrink(move);
            }
        }
        return toAdd;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, inv.getItems(), registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, inv.getItems(), registries);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new DispenserMenu(id, inventory, this.inv);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.unloader");
    }
}
