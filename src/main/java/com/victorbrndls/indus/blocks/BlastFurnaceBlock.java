package com.victorbrndls.indus.blocks;

import com.mojang.serialization.MapCodec;
import com.victorbrndls.indus.blocks.tileentity.BlastFurnaceBlockEntity;
import com.victorbrndls.indus.blocks.tileentity.IndusTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlastFurnaceBlock extends BaseStructureBlock {

    public static final MapCodec<BlastFurnaceBlock> CODEC = simpleCodec(BlastFurnaceBlock::new);

    public BlastFurnaceBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlastFurnaceBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<BlastFurnaceBlockEntity> getBlockEntityType() {
        return IndusTileEntities.BLAST_FURNACE.get();
    }

}