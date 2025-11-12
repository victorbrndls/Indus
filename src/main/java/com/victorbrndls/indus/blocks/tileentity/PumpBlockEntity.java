package com.victorbrndls.indus.blocks.tileentity;

import com.mojang.serialization.Codec;
import com.victorbrndls.indus.items.IndusItems;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.world.IndusEnergyManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class PumpBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos OUTPUT_POS = new BlockPos(4, 0, 0);

    private final static int ENERGY_CONSUMPTION = 1;
    private final static int RATE = 1;

    private long networkId = -1;

    public PumpBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.PUMP.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.PUMP;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        if ((level.getGameTime() % 100) != 0) return;

        if (networkId < 0) return;
        var energyManager = IndusEnergyManager.get((ServerLevel) level);

        ResourceHandler<ItemResource> handler = getRelativeItemHandler(level, OUTPUT_POS);
        if (handler == null) return;

        try (Transaction tx = Transaction.open(null)) {
            long inserted = handler.insert(ItemResource.of(IndusItems.WATER_CELL.get()), RATE, tx);
            if (inserted == 1) {
                var consumed = energyManager.consumeEnergy(networkId, ENERGY_CONSUMPTION);
                if (consumed) {
                    tx.commit();
                }
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("networkId", Codec.LONG, networkId);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        networkId = input.getLongOr("networkId", -1L);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.pump");
    }
}