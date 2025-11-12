package com.victorbrndls.indus.blocks;

import com.mojang.serialization.MapCodec;
import com.victorbrndls.indus.blocks.tileentity.IndusTileEntities;
import com.victorbrndls.indus.blocks.tileentity.MixerBlockEntity;
import com.victorbrndls.indus.items.IndusItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class MixerBlock extends BaseStructureBlock {

    public static final MapCodec<MixerBlock> CODEC = simpleCodec(MixerBlock::new);

    public MixerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.getMainHandItem().is(IndusItems.WRENCH.get())) return InteractionResult.PASS;

        if (player instanceof ServerPlayer serverPlayer) {
            level.getBlockEntity(pos, IndusTileEntities.MIXER.get())
                    .ifPresent(blockEntity -> serverPlayer.openMenu(blockEntity, pos));
        }
        return InteractionResult.SUCCESS_SERVER;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MixerBlockEntity(pos, state);
    }

}