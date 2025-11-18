package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.items.IndusItems;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.world.IndusNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class PumpBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos OUTPUT_POS = new BlockPos(4, 0, 0);

    private final static int ENERGY_CONSUMPTION = 8;
    private final static int RATE = 8;

    public PumpBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.PUMP.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.PUMP;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        if ((level.getGameTime() % 80) != 0) return;

        if (networkId < 0) return;
        var energyManager = IndusNetworkManager.get((ServerLevel) level);

        var handler = getRelativeItemHandler(level, OUTPUT_POS);
        if (handler == null) return;

        ItemStack output = new ItemStack(IndusItems.WATER_CELL.get(), RATE);

        var remainder = ItemHandlerHelper.insertItem(handler, output, true);
        if (!remainder.isEmpty()) return;

        var energy = energyManager.getEnergy(networkId);
        if (energy < ENERGY_CONSUMPTION) return;

        ItemHandlerHelper.insertItem(handler, output, false);
        energyManager.consumeEnergy(networkId, ENERGY_CONSUMPTION);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.pump");
    }
}