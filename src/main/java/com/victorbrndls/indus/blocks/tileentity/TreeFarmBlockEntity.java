package com.victorbrndls.indus.blocks.tileentity;

import com.mojang.serialization.Codec;
import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.blocks.structure.*;
import com.victorbrndls.indus.gui.TreeFarmMenu;
import com.victorbrndls.indus.shared.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
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

import static com.victorbrndls.indus.blocks.TreeFarmBlock.FACING;

public class TreeFarmBlockEntity extends BlockEntity implements MenuProvider {

    private int tickCounter = 0;

    private StructureState state = StructureState.NOT_READY;

    // Structure info used during construction
    private IndusStructureInfo structureInfo;
    private int lastBuiltIndex = 0;

    public TreeFarmBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.TREE_FARM_BLOCK_ENTITY.get(), pos, state);
    }

    public StructureState getState() {
        return state;
    }

    public void setState(StructureState state) {
        Indus.LOGGER.debug("Changing state to {}", state);

        if (this.state == state) return;
        this.state = state;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        switch (getState()) {
            case NOT_READY -> tickNotReady(level, pos, state);
            case IN_CONSTRUCTION -> tickInConstruction(level, pos, state);
            case BUILT -> tickBuilt(level, pos, state);
        }
    }

    private void tickNotReady(Level level, BlockPos pos, BlockState state) {
        // nothing to do
    }

    private void tickInConstruction(Level level, BlockPos pos, BlockState state) {
        tickCounter++;

        if (tickCounter < 5) return;
        tickCounter = 0;

        if (structureInfo == null) {
            structureInfo = IndusStructureHelper.getOrLoadStructureInfo(level.getServer(), IndusStructure.TREE_FARM);
            if (structureInfo == null) {
                Indus.LOGGER.error("Could not load structure info");
                return;
            }
        }

        lastBuiltIndex = IndusStructureBuilder.build(
                structureInfo, level, pos, state.getValue(FACING).getOpposite(), lastBuiltIndex
        );

        if (lastBuiltIndex == Integer.MAX_VALUE) {
            setState(StructureState.BUILT);
        }
    }

    private void tickBuilt(Level level, BlockPos pos, BlockState state) {
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

    public void startBuilding() {
        Indus.LOGGER.debug("Starting construction at {}", getBlockPos());

        BlockPos inputPos = inputPos();
        BlockEntity be = getLevel().getBlockEntity(inputPos);

        var requirements = IndusStructureRequirements.getRequirements(IndusStructure.TREE_FARM);

        // Vanilla-compatible path
        if (be instanceof Container c) {
            for (ItemStack req : requirements) {
                int remaining = req.getCount();
                for (int i = 0; i < c.getContainerSize() && remaining > 0; i++) {
                    ItemStack s = c.getItem(i);
                    if (s.isEmpty() || !ItemStack.isSameItemSameComponents(s, req)) continue;

                    int take = Math.min(s.getCount(), remaining);
                    s.shrink(take);
                    remaining -= take;

                    if (s.isEmpty()) c.setItem(i, ItemStack.EMPTY);
                }
            }
        } else {
            throw new UnsupportedOperationException();
        }

        setState(StructureState.IN_CONSTRUCTION);
    }

    public BlockPos inputPos() {
        return BlockHelper.offsetFrontFacing(getBlockPos(), getBlockState(), 0, 0, 1);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("state", Codec.INT, state.ordinal());

        if (state == StructureState.IN_CONSTRUCTION) {
            output.store("lastBuiltIndex", Codec.INT, lastBuiltIndex);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.read("state", Codec.INT)
                .ifPresent(i -> this.state = StructureState.values()[i]);

        if (state == StructureState.IN_CONSTRUCTION) {
            input.read("lastBuiltIndex", Codec.INT).ifPresent(i -> this.lastBuiltIndex = i);
        }
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
}