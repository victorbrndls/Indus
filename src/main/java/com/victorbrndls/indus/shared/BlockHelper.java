package com.victorbrndls.indus.shared;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BlockHelper {

    public static BlockPos offsetFrontFacing(BlockPos origin, BlockState state, int forward, int right, int up) {
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING); // ensure your block has this property
        return origin
                .relative(facing, -forward) // front is towards the player but we want the opposite
                .relative(facing.getClockWise(), -right) // front is towards the player but we want the opposite
                .above(up);
    }


}
