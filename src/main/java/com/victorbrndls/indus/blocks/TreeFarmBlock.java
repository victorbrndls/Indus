package com.victorbrndls.indus.blocks;

import com.mojang.serialization.MapCodec;
import com.victorbrndls.indus.IndusClient;
import com.victorbrndls.indus.blocks.structure.IndusStructure;
import com.victorbrndls.indus.blocks.structure.IndusStructureHelper;
import com.victorbrndls.indus.blocks.tileentity.TreeFarmBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TreeFarmBlock extends BaseEntityBlock {

    public static final MapCodec<TreeFarmBlock> CODEC = simpleCodec(TreeFarmBlock::new);

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public TreeFarmBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        // front faces the player
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return rotate(state, mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (level.isClientSide()) return;
        placeStructure(level, pos, placer);
    }

    private void placeStructure(Level level, BlockPos pos, LivingEntity entity) {
        var serverLevel = (ServerLevel) level;

        var structure = IndusClient.STRUCTURE_CACHE.get(IndusStructure.TREE_FARM);
        var orientation = IndusStructureHelper.getOrientation(IndusStructure.TREE_FARM, entity.getDirection());

        List<BlockPos> positions = structure.pos();
        List<BlockState> states = structure.blockState();

        Vec3 pivot = orientation.getCenter();
        BlockPos offset = BlockPos.containing(fromI(orientation.getOffset()));

        for (int i = 0; i < positions.size(); i++) {
            BlockState state = states.get(i);
            if (state.isAir()) continue;

            BlockPos rel = positions.get(i);
            BlockPos relRot = rotateAroundPivot(rel, pivot, toRotation(orientation.rotationDegrees()));

            BlockPos worldPos = pos.offset(offset).offset(relRot);

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

    private static BlockPos rotateAroundPivot(BlockPos p, Vec3 pivot, Rotation rot) {
        double x = p.getX() + 0.5;
        double z = p.getZ() + 0.5;
        double cx = pivot.x();
        double cz = pivot.z();

        double dx = x - cx;
        double dz = z - cz;

        double rx = dx, rz = dz;
        switch (rot) {
            case CLOCKWISE_90 -> {
                rx = dz;
                rz = -dx;
            }
            case CLOCKWISE_180 -> {
                rx = -dx;
                rz = -dz;
            }
            case COUNTERCLOCKWISE_90 -> {
                rx = -dz;
                rz = dx;
            }
            case NONE -> { /* no-op */ }
        }

        double nx = cx + rx;
        double nz = cz + rz;

        // back to block corner from center
        int ix = Mth.floor(nx - 0.5);
        int iz = Mth.floor(nz - 0.5);
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

    private static Vec3 fromI(Vec3i vec) {
        return new Vec3(vec.getX(), vec.getY(), vec.getZ());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TreeFarmBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return (lvl, pos, st, te) -> {
            if (te instanceof TreeFarmBlockEntity be) be.tick(lvl, pos, st);
        };
    }
}