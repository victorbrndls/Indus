package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.items.IndusItems;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class PumpBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos OUTPUT_POS = new BlockPos(4, 0, 0);

    public PumpBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.PUMP.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.PUMP;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        tickCounter++;

        if (tickCounter >= 100) {
            tickCounter = 0;

            ResourceHandler<ItemResource> handler = getRelativeItemHandler(level, OUTPUT_POS);

            if (handler != null) {
                try (Transaction tx = Transaction.open(null)) {
                    long inserted = handler.insert(ItemResource.of(IndusItems.WATER_CELL.get()), 1, tx);
                    if (inserted == 1) tx.commit();
                }
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.pump");
    }
}