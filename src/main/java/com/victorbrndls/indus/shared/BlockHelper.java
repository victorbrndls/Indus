package com.victorbrndls.indus.shared;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class BlockHelper {

    public static BlockPos offsetFrontFacing(BlockPos origin, BlockState state, int forward, int right, int up) {
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING); // ensure your block has this property
        return origin
                .relative(facing, -forward) // front is towards the player but we want the opposite
                .relative(facing.getClockWise(), -right) // front is towards the player but we want the opposite
                .above(up);
    }

    public static BlockPos offsetFrontFacing(BlockPos origin, BlockState state, BlockPos relative) {
        return offsetFrontFacing(origin, state, relative.getX(), relative.getZ(), relative.getY());
    }

    @Nullable
    public static IItemHandler getItemHandlerAt(Level level, BlockPos pos) {
        if (!level.isLoaded(pos)) return null;

        BlockState ciBs = level.getBlockState(pos);
        BlockEntity ciTe = level.getBlockEntity(pos);
        if (ciTe == null) return null;


        return level.getCapability(Capabilities.ItemHandler.BLOCK, pos, ciBs, ciTe, null);
    }

}
