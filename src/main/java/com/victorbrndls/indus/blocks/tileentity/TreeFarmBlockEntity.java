package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.mod.structure.IndusStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class TreeFarmBlockEntity extends BaseStructureBlockEntity {

    private static final BlockPos OUTPUT_POS = new BlockPos(7, 1, 0);

    public TreeFarmBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.TREE_FARM.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.TREE_FARM;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        if ((level.getGameTime() % 80) != 0) return;

        ResourceHandler<ItemResource> handler = getRelativeItemHandler(level, OUTPUT_POS);
        if (handler == null) return;

        try (Transaction tx = Transaction.open(null)) {
            long inserted = handler.insert(ItemResource.of(Items.OAK_LOG), 1, tx);
            if (inserted == 1) tx.commit();
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.tree_farm");
    }
}