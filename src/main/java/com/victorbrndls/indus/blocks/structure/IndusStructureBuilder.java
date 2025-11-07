package com.victorbrndls.indus.blocks.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class IndusStructureBuilder {

    public static int build(
            IndusStructureInfo info,
            Level level,
            BlockPos pos,
            Direction direction,
            int index
    ) {
        var serverLevel = (ServerLevel) level;

        var orientation = IndusStructureHelper.getOrientation(info.structure(), direction);

        List<BlockPos> positions = info.pos();
        List<BlockState> states = info.blockState();

        if (index >= positions.size()) {
            return Integer.MAX_VALUE;
        }

        BlockPos offset = BlockPos.containing(orientation.getOffset());

        BlockPos rel = positions.get(index);
        BlockPos relRot = rotateAroundPivot(rel, toRotation(orientation.rotationDegrees()));

        BlockPos worldPos = pos.offset(offset).offset(relRot);

        BlockState state = states.get(index);
        state = rotateState(level, relRot, state, toRotation(360 - orientation.rotationDegrees()));

        if (!serverLevel.isLoaded(worldPos)) return index;
        if (!serverLevel.isInsideBuildHeight(worldPos.getY())) return index + 1;

        serverLevel.setBlock(worldPos, state, Block.UPDATE_ALL);

        if (states.get(index).isAir()) {
            // If we placed air, continue immediately
            return build(info, level, pos, direction, index + 1);
        }

        return index + 1;
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
