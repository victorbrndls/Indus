package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.shared.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class TreeFarmBlockEntity extends BaseStructureBlockEntity {

    public TreeFarmBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.TREE_FARM.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.TREE_FARM;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
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
    public Component getDisplayName() {
        return Component.translatable("block.indus.tree_farm");
    }
}