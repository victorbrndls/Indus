package com.victorbrndls.indus.mod.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class IndusStructureBuilder {

    public static int destroyAndBuild(
            IndusStructureInfo info,
            Level level,
            BlockPos pos,
            Direction direction,
            int index
    ) {
        if (index >= info.pos().size()) {
            return build(info, level, pos, direction, index);
        } else {
            // destroy 2 blocks so it's faster
            var newIndex = destroy(info, level, pos, direction, index);
            return destroy(info, level, pos, direction, newIndex);
        }
    }

    public static int build(
            IndusStructureInfo info,
            Level level,
            BlockPos pos,
            Direction direction,
            int index
    ) {
        var arrayIndex = index - info.pos().size();

        List<BlockPos> positions = info.pos();
        List<BlockState> states = info.blockState();

        if (arrayIndex >= positions.size()) {
            return Integer.MAX_VALUE;
        }

        var serverLevel = (ServerLevel) level;
        var orientation = IndusStructureHelper.getOrientation(info.structure(), direction);

        BlockPos offset = BlockPos.containing(orientation.getOffset());
        BlockPos rel = positions.get(arrayIndex);
        BlockPos relRot = rotateAroundPivot(rel, toRotation(orientation.rotationDegrees()));

        BlockPos worldPos = pos.offset(offset).offset(relRot);

        BlockState state = states.get(arrayIndex);
        state = rotateState(level, relRot, state, toRotation(360 - orientation.rotationDegrees()));

        if (!serverLevel.isLoaded(worldPos)) return index;
        if (!serverLevel.isInsideBuildHeight(worldPos.getY())) return index + 1;

        serverLevel.setBlock(worldPos, state, Block.UPDATE_ALL);

        if (states.get(arrayIndex).isAir()) {
            // If we placed air, continue immediately
            return build(info, level, pos, direction, index + 1);
        }

        return index + 1;
    }

    public static int destroy(
            IndusStructureInfo info,
            Level level,
            BlockPos pos,
            Direction direction,
            int index
    ) {
        List<BlockPos> positions = info.pos();

        var arrayIndex = positions.size() - index - 1;
        if (arrayIndex < 0) return index;

        var serverLevel = (ServerLevel) level;
        var orientation = IndusStructureHelper.getOrientation(info.structure(), direction);

        BlockPos offset = BlockPos.containing(orientation.getOffset());
        BlockPos rel = positions.get(arrayIndex);
        BlockPos relRot = rotateAroundPivot(rel, toRotation(orientation.rotationDegrees()));

        BlockPos worldPos = pos.offset(offset).offset(relRot);

        if (!serverLevel.isLoaded(worldPos)) return index;
        if (!serverLevel.isInsideBuildHeight(worldPos.getY())) return index + 1;

        var existingBlock = level.getBlockState(worldPos);

        if (existingBlock.getBlock() instanceof BaseEntityBlock) {
            // don't destroy tile entities
            return destroy(info, level, pos, direction, index + 1);
        } else if (existingBlock.isAir()) {
            // if it's air, continue immediately
            return destroy(info, level, pos, direction, index + 1);
        } else {
            serverLevel.setBlock(worldPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            return index + 1;
        }
    }

    private static Rotation toRotation(int deg) {
        return switch (Math.floorMod(deg, 360)) {
            case 90 -> Rotation.CLOCKWISE_90;
            case 180 -> Rotation.CLOCKWISE_180;
            case 270 -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };
    }

    private static BlockPos rotateAroundPivot(BlockPos p, Rotation rot) {
        double x = p.getX() + 0.5;
        double z = p.getZ() + 0.5;

        double rx = x, rz = z;
        switch (rot) {
            case CLOCKWISE_90 -> {
                rx = z;
                rz = -x;
            }
            case CLOCKWISE_180 -> {
                rx = -x;
                rz = -z;
            }
            case COUNTERCLOCKWISE_90 -> {
                rx = -z;
                rz = x;
            }
            case NONE -> { /* no-op */ }
        }

        // back to block corner from center
        int ix = Mth.floor(rx - 0.5);
        int iz = Mth.floor(rz - 0.5);
        return new BlockPos(ix, p.getY(), iz);
    }

    // Works for vanilla facings; no-op for blocks without rotation logic.
    private static BlockState rotateState(
            LevelAccessor level,
            BlockPos pos,
            BlockState state,
            Rotation rot) {
        if (rot == Rotation.NONE) return state;
        try {
            return state.rotate(level, pos, rot);
        } catch (Throwable ignored) {
            return state;
        }
    }

}
