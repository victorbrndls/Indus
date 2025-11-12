package com.victorbrndls.indus.blocks.tileentity;


import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class Container1BlockEntity extends BlockEntity {

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

}
