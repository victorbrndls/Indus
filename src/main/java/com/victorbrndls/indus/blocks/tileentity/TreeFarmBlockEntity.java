package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class TreeFarmBlockEntity extends BlockEntity {

    private int tickCounter = 0;

    public TreeFarmBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.TREE_FARM_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }

        tickCounter++;

        if (tickCounter >= 20) {
            tickCounter = 0;

            BlockPos target = BlockHelper.offsetFrontFacing(pos, state, 2, 1, 0);
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
}