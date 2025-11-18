package com.victorbrndls.indus.blocks;

import com.mojang.serialization.MapCodec;
import com.victorbrndls.indus.blocks.tileentity.Assembler1BlockEntity;
import com.victorbrndls.indus.blocks.tileentity.IndusTileEntities;
import com.victorbrndls.indus.blocks.tileentity.TreeFarmBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TreeFarmBlock extends BaseStructureBlock {

    public static final MapCodec<TreeFarmBlock> CODEC = simpleCodec(TreeFarmBlock::new);

    public TreeFarmBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TreeFarmBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<TreeFarmBlockEntity> getBlockEntityType() {
        return IndusTileEntities.TREE_FARM.get();
    }

}