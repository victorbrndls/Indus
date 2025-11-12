package com.victorbrndls.indus.blocks.tileentity;


import com.victorbrndls.indus.gui.Container1Menu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class Container1BlockEntity extends BlockEntity implements MenuProvider {

    private final SimpleContainer inv = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            Container1BlockEntity.this.setChanged();
        }
    };

    public Container1BlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.CONTAINER_1.get(), pos, state);
    }

    public SimpleContainer getContainer() {
        return inv;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("items", ItemContainerContents.CODEC, ItemContainerContents.fromItems(inv.getItems()));
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.read("items", ItemContainerContents.CODEC).ifPresent(items -> {
            for (int i = 0; i < inv.getContainerSize(); i++) {
                inv.setItem(i, items.getStackInSlot(i));
            }
        });
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new Container1Menu(id, inventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.container_1");
    }
}
