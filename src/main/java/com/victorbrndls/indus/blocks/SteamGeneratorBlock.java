package com.victorbrndls.indus.blocks;

import com.mojang.serialization.MapCodec;
import com.victorbrndls.indus.blocks.tileentity.IndusTileEntities;
import com.victorbrndls.indus.blocks.tileentity.SteamGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SteamGeneratorBlock extends BaseStructureBlock {

    public static final MapCodec<SteamGeneratorBlock> CODEC = simpleCodec(SteamGeneratorBlock::new);

    public SteamGeneratorBlock(Properties properties) {
        super(properties);

    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SteamGeneratorBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<SteamGeneratorBlockEntity> getBlockEntityType() {
        return IndusTileEntities.STEAM_GENERATOR.get();
    }

}