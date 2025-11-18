package com.victorbrndls.indus.blocks;

import com.mojang.serialization.MapCodec;
import com.victorbrndls.indus.blocks.tileentity.IndusTileEntities;
import com.victorbrndls.indus.blocks.tileentity.MixerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MixerBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<MixerBlockEntity> getBlockEntityType() {
        return IndusTileEntities.MIXER.get();
    }

}