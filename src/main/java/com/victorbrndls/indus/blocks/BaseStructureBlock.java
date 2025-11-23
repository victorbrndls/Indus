package com.victorbrndls.indus.blocks;

import com.victorbrndls.indus.IndusClient;
import com.victorbrndls.indus.blocks.tileentity.BaseStructureBlockEntity;
import com.victorbrndls.indus.items.IndusItems;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class BaseStructureBlock extends BaseEntityBlock {

    private final IndusStructure structure;

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BaseStructureBlock(
            IndusStructure structure,
            Properties properties
    ) {
        super(properties.strength(4.0F, 6.0F));
        this.structure = structure;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
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
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(IndusItems.WRENCH.get())) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (stack.is(Items.BEDROCK)) {
            if (level.getBlockEntity(pos) instanceof BaseStructureBlockEntity be) {
                be.startBuilding(true);
                return ItemInteractionResult.SUCCESS;
            }
        }

        if (stack.isEmpty() && player.isShiftKeyDown()) {
            if (level.isClientSide()) {
                IndusClient.GHOST_STRUCTURES.toggleFixedGhost(pos, state.getValue(FACING).getOpposite(), structure);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        if (!player.isShiftKeyDown()) {
            if (player instanceof ServerPlayer serverPlayer) {
                level.getBlockEntity(pos, getBlockEntityType())
                        .ifPresent(blockEntity -> serverPlayer.openMenu(blockEntity, pos));
            }
        }

        if (level.isClientSide()) {
            IndusClient.GHOST_STRUCTURES.removeFixedGhost(pos);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (level.isClientSide()) {
            IndusClient.GHOST_STRUCTURES.removeFixedGhost(pos);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return (lvl, pos, st, te) -> {
            if (te instanceof BaseStructureBlockEntity be) be.tick(lvl, pos, st);
        };
    }

    protected abstract <T extends BaseStructureBlockEntity> BlockEntityType<T> getBlockEntityType();
}