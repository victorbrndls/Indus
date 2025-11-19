package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.blocks.CartLoaderBlock;
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

public class LoaderBlockEntity extends BlockEntity implements MenuProvider {

    private final SimpleContainer inv = new SimpleContainer(9) {
        @Override
        public void setChanged() {
            LoaderBlockEntity.this.setChanged();
        }
    };

    public LoaderBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.CART_LOADER.get(), pos, state);
    }

    public SimpleContainer getContainer() {
        return inv;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LoaderBlockEntity blockEntity) {
        if (level.isClientSide) return;

        // Find Hopper Minecart below
        List<MinecartHopper> minecarts = level.getEntitiesOfClass(MinecartHopper.class, new AABB(pos.below()));
        if (minecarts.isEmpty()) {
            if (state.getValue(CartLoaderBlock.POWERED)) {
                level.setBlock(pos, state.setValue(CartLoaderBlock.POWERED, false), 3);
            }
            return;
        }

        MinecartHopper minecart = minecarts.get(0);
        
        // Try to move items from this inventory to minecart
        moveItems(blockEntity.inv, minecart);

        boolean isMinecartFull = isFull(minecart);
        
        if (state.getValue(CartLoaderBlock.POWERED) != isMinecartFull) {
            level.setBlock(pos, state.setValue(CartLoaderBlock.POWERED, isMinecartFull), 3);
        }
    }

    private static void moveItems(SimpleContainer source, MinecartHopper target) {
        for (int i = 0; i < source.getContainerSize(); i++) {
            ItemStack sourceStack = source.getItem(i);
            if (sourceStack.isEmpty()) continue;

            ItemStack remaining = addItem(target, sourceStack);
            if (remaining.getCount() != sourceStack.getCount()) {
                source.setItem(i, remaining);
                source.setChanged();
                target.setChanged();
            }
        }
    }

    // Helper to add item to minecart (which is a Container)
    private static ItemStack addItem(MinecartHopper container, ItemStack stack) {
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

    private static boolean isFull(MinecartHopper minecart) {
        for (int i = 0; i < minecart.getContainerSize(); i++) {
            ItemStack stack = minecart.getItem(i);
            if (stack.isEmpty()) return false;
            if (stack.getCount() < stack.getMaxStackSize()) return false;
        }
        return true;
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
        return Component.translatable("block.indus.loader");
    }
}
