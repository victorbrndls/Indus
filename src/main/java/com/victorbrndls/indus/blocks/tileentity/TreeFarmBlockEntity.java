package com.victorbrndls.indus.blocks.tileentity;

import com.mojang.serialization.Codec;
import com.victorbrndls.indus.blocks.structure.StructureState;
import com.victorbrndls.indus.inventory.TreeFarmMenu;
import com.victorbrndls.indus.shared.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

public class TreeFarmBlockEntity extends BlockEntity implements MenuProvider {

    private int tickCounter = 0;

    private StructureState state = StructureState.NOT_READY;

    public TreeFarmBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.TREE_FARM_BLOCK_ENTITY.get(), pos, state);
    }

    public StructureState getState() {
        return state;
    }

    public void setState(StructureState state) {
        if (this.state == state) return;
        this.state = state;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;
        if (getState() != StructureState.BUILT) return;

        tickCounter++;

        if (tickCounter >= 100) {
            tickCounter = 0;

            BlockPos target = BlockHelper.offsetFrontFacing(pos, state, 7, 0, 1);
            if (!level.isLoaded(target)) return;

            BlockState ts = level.getBlockState(target);
            BlockEntity te = level.getBlockEntity(target);
            if (te == null) return;

            ResourceHandler<ItemResource> handler = level.getCapability(Capabilities.Item.BLOCK, target, ts, te, null);

            if (handler != null) {
                try (Transaction tx = Transaction.open(null)) {
                    long inserted = handler.insert(ItemResource.of(Items.OAK_LOG), 1, tx);
                    if (inserted == 1) tx.commit();
                }
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("state", Codec.INT, state.ordinal());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.read("state", Codec.INT)
                .ifPresent(i -> this.state = StructureState.values()[i]);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        TagValueOutput valueOutput = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, provider);
        saveAdditional(valueOutput);
        return valueOutput.buildResult();
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        super.handleUpdateTag(input);
        loadAdditional(input);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new TreeFarmMenu(id, inventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.tree_farm");
    }

    public BlockPos inputPos() {
        return BlockHelper.offsetFrontFacing(getBlockPos(), getBlockState(), 2, 0, 1);
    }
}