package com.victorbrndls.indus.blocks.tileentity;

import com.mojang.serialization.Codec;
import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.gui.BaseStructureMenu;
import com.victorbrndls.indus.mod.structure.*;
import com.victorbrndls.indus.shared.BlockHelper;
import com.victorbrndls.indus.world.IndusNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.Nullable;

import static com.victorbrndls.indus.blocks.TreeFarmBlock.FACING;

public abstract class BaseStructureBlockEntity extends BlockEntity implements MenuProvider {

    protected IndusStructureState state = IndusStructureState.NOT_READY;

    // Structure info used during construction
    private IndusStructureInfo structureInfo;
    private int lastBuiltIndex = 0;
    private boolean instaBuild = false; // only used in debug

    // Only after built state
    protected long networkId = -1;

    public BaseStructureBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract IndusStructure getStructureType();

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
        if ((level.getGameTime() % 5) != 0 && !instaBuild) return;

        if (structureInfo == null) {
            structureInfo = IndusStructureHelper.getOrLoadStructureInfo(level.getServer(), getStructureType());
            if (structureInfo == null) {
                Indus.LOGGER.error("Could not load structure info");
                return;
            }
        }

        lastBuiltIndex = IndusStructureBuilder.destroyAndBuild(
                structureInfo, level, pos, state.getValue(FACING).getOpposite(), lastBuiltIndex
        );

        if (lastBuiltIndex == Integer.MAX_VALUE) {
            onAfterBuilt(level, pos, state);
            setState(IndusStructureState.BUILT);
        }
    }

    protected void onAfterBuilt(Level level, BlockPos pos, BlockState state) {
        var energyManager = IndusNetworkManager.get((ServerLevel) level);

        if (networkId <= 0) {
            networkId = energyManager.getNetworkId();
        }
    }

    protected abstract void tickBuilt(Level level, BlockPos pos, BlockState state);

    public void startBuilding(boolean instaBuild) {
        Indus.LOGGER.debug("Starting construction at {}", getBlockPos());
        this.instaBuild = instaBuild;

        BlockPos inputPos = inputPos();
        BlockEntity be = getLevel().getBlockEntity(inputPos);

        if (!instaBuild) {
            var requirements = IndusStructureRequirements.getRequirements(getStructureType());

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
        }

        setState(IndusStructureState.IN_CONSTRUCTION);
    }

    public BlockPos inputPos() {
        return BlockHelper.offsetFrontFacing(getBlockPos(), getBlockState(), 0, 0, 1);
    }

    protected ResourceHandler<ItemResource> getRelativeItemHandler(Level level, BlockPos relativePos) {
        var targetPos = BlockHelper.offsetFrontFacing(getBlockPos(), getBlockState(), relativePos);
        return BlockHelper.getItemHandlerAt(level, targetPos);
    }

    public IndusStructureState getState() {
        return state;
    }

    public void setState(IndusStructureState state) {
        Indus.LOGGER.debug("Changing state to {}", state);

        if (this.state == state) return;
        this.state = state;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    public long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(long networkId) {
        if (this.networkId == networkId) return;
        Indus.LOGGER.debug("Changing network ID to {}", networkId);

        onDisconnectFromNetwork();
        this.networkId = networkId;
        onConnectedToNetwork();

        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    protected void onDisconnectFromNetwork() {
    }

    protected void onConnectedToNetwork() {
    }

    protected boolean canInteractWithNetwork() {
        return level instanceof ServerLevel && networkId > 0 && state == IndusStructureState.BUILT;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("state", Codec.INT, state.ordinal());

        if (state == IndusStructureState.IN_CONSTRUCTION) {
            output.store("lastBuiltIndex", Codec.INT, lastBuiltIndex);
        } else if (state == IndusStructureState.BUILT) {
            output.store("networkId", Codec.LONG, networkId);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.read("state", Codec.INT)
                .ifPresent(i -> state = IndusStructureState.values()[i]);

        if (state == IndusStructureState.IN_CONSTRUCTION) {
            input.read("lastBuiltIndex", Codec.INT).ifPresent(i -> lastBuiltIndex = i);
        } else if (state == IndusStructureState.BUILT) {
            input.read("networkId", Codec.LONG).ifPresent(l -> networkId = l);
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
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BaseStructureMenu(id, inventory, this);
    }
}