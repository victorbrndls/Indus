package com.victorbrndls.indus.blocks;

import com.victorbrndls.indus.blocks.tileentity.TreeFarmBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TreeFarmBlock extends Block implements EntityBlock {

    public TreeFarmBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TreeFarmBlockEntity(blockPos, blockState);
    }

}
