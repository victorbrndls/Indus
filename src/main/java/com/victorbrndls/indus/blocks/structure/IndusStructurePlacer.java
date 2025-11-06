package com.victorbrndls.indus.blocks.structure;

import com.victorbrndls.indus.Indus;
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

public class IndusStructurePlacer {

    public static void placeStructure(
            IndusStructure struct,
            Level level,
            BlockPos pos,
            Direction direction
    ) {
        var serverLevel = (ServerLevel) level;

        var structure = Indus.STRUCTURE_CACHE.get(struct);
        if (structure == null) {
            structure = IndusStructureHelper.loadStructureInfo(level.getServer(), struct).orElse(null);
            if (structure == null) {
                Indus.LOGGER.error("Failed to load structure: {}", struct);
                return;
            }
        }

        var orientation = IndusStructureHelper.getOrientation(struct, direction);

        List<BlockPos> positions = structure.pos();
        List<BlockState> states = structure.blockState();

        BlockPos offset = BlockPos.containing(orientation.getOffset());

        for (int i = 0; i < positions.size(); i++) {
            BlockPos rel = positions.get(i);
            BlockPos relRot = rotateAroundPivot(rel, toRotation(orientation.rotationDegrees()));

            BlockPos worldPos = pos.offset(offset).offset(relRot);

            BlockState state = states.get(i);
            state = rotateState(level, relRot, state, toRotation(360 - orientation.rotationDegrees()));

            if (!serverLevel.isLoaded(worldPos)) continue;
            if (!serverLevel.isInsideBuildHeight(worldPos.getY())) continue;

            serverLevel.setBlock(worldPos, state, Block.UPDATE_ALL);
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
