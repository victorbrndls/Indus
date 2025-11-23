package com.victorbrndls.indus.blocks;

import com.mojang.serialization.MapCodec;
import com.victorbrndls.indus.blocks.tileentity.Assembler1BlockEntity;
import com.victorbrndls.indus.blocks.tileentity.IndusTileEntities;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class Assembler1Block extends BaseStructureBlock {

    public static final MapCodec<Assembler1Block> CODEC = simpleCodec(Assembler1Block::new);

    public Assembler1Block(Properties properties) {
        super(IndusStructure.ASSEMBLER_1, properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new Assembler1BlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<Assembler1BlockEntity> getBlockEntityType() {
        return IndusTileEntities.ASSEMBLER_1.get();
    }
}