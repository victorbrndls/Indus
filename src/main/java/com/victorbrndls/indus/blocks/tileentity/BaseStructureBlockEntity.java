package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.gui.BaseStructureMenu;
import com.victorbrndls.indus.items.MaintenanceTier;
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
import net.neoforged.neoforge.items.IItemHandler;
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
        var networkManager = IndusNetworkManager.get((ServerLevel) level);

        if (networkId <= 0) {
            networkId = networkManager.getNetworkId();
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

    protected IItemHandler getRelativeItemHandler(Level level, BlockPos relativePos) {
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

    /**
     * @return true if the machine should run
     */
    protected boolean consumeMaintenanceAndCheck(MaintenanceTier basic, int maintenanceRate) {
        if (networkId < 0) return false;
        var networkManager = IndusNetworkManager.get((ServerLevel) level);

        networkManager.consumeMaintenance(networkId, basic, maintenanceRate);
        var remainingMaintenance = networkManager.getRemainingMaintenancePercentage(networkId, basic);

        if (remainingMaintenance > 0.85) {
            return true;
        } else if (remainingMaintenance > 0.60) {
            return Indus.RANDOM.nextFloat() >= 0.10f;
        } else if (remainingMaintenance > 0.35) {
            return Indus.RANDOM.nextFloat() >= 0.35f;
        } else {
            return Indus.RANDOM.nextFloat() >= 0.75f;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("state", state.ordinal());

        if (state == IndusStructureState.IN_CONSTRUCTION) {
            tag.putInt("lastBuiltIndex", lastBuiltIndex);
        } else if (state == IndusStructureState.BUILT) {
            tag.putLong("networkId", networkId);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        state = IndusStructureState.values()[tag.getInt("state")];

        if (state == IndusStructureState.IN_CONSTRUCTION) {
            lastBuiltIndex = tag.getInt("lastBuiltIndex");
        } else if (state == IndusStructureState.BUILT) {
            networkId = tag.getLong("networkId");
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        loadAdditional(tag, lookupProvider);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
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