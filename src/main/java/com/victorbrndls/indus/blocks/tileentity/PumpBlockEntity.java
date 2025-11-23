package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.mod.structure.IndusStructureStatus;
import com.victorbrndls.indus.shared.FluidLocator;
import com.victorbrndls.indus.world.IndusNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

public class PumpBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos OUTPUT_POS = new BlockPos(4, 0, 0);

    private final static int ENERGY_CONSUMPTION = 8;
    private final static int RATE = 8;

    @Nullable
    private Item prospectedFluid = null;
    private boolean hasProspected = false;

    public PumpBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.PUMP.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.PUMP;
    }

    private Item getResource() {
        if (!hasProspected) {
            prospectedFluid = FluidLocator.prospect(level, getBlockPos());
            hasProspected = true;
        }

        return prospectedFluid;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        if ((level.getGameTime() % 80) != 0) return;

        var resource = getResource();
        if (resource == null) {
            setStatus(IndusStructureStatus.NO_RESOURCE);
            return;
        }

        if (networkId < 0) {
            setStatus(IndusStructureStatus.NOT_CONNECTED);
            return;
        }
        var networkManager = IndusNetworkManager.get((ServerLevel) level);

        var handler = getRelativeItemHandler(level, OUTPUT_POS);
        if (handler == null) {
            setStatus(IndusStructureStatus.INVALID_STRUCTURE);
            return;
        }

        ItemStack output = new ItemStack(resource, RATE);

        var remainder = ItemHandlerHelper.insertItem(handler, output, true);
        if (!remainder.isEmpty()) {
            setStatus(IndusStructureStatus.OUTPUT_FULL);
            return;
        }

        var energy = networkManager.getEnergy(networkId);
        if (energy < ENERGY_CONSUMPTION) {
            setStatus(IndusStructureStatus.NO_ENERGY);
            return;
        }

        setStatus(IndusStructureStatus.WORKING);
        ItemHandlerHelper.insertItem(handler, output, false);
        networkManager.consumeEnergy(networkId, ENERGY_CONSUMPTION);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.pump");
    }
}