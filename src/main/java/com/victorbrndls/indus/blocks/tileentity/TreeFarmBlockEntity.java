package com.victorbrndls.indus.blocks.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TreeFarmBlockEntity extends BlockEntity {

    public TreeFarmBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.TREE_FARM_BLOCK_ENTITY.get(), pos, state);
    }

}