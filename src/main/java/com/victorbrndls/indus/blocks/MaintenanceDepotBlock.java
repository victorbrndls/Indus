package com.victorbrndls.indus.blocks;

import com.mojang.serialization.MapCodec;
import com.victorbrndls.indus.blocks.tileentity.IndusTileEntities;
import com.victorbrndls.indus.blocks.tileentity.MaintenanceDepotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MaintenanceDepotBlock extends BaseStructureBlock {

    public static final MapCodec<MaintenanceDepotBlock> CODEC = simpleCodec(MaintenanceDepotBlock::new);

    public MaintenanceDepotBlock(Properties properties) {
        super(properties);

    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MaintenanceDepotBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<MaintenanceDepotBlockEntity> getBlockEntityType() {
        return IndusTileEntities.MAINTENANCE_DEPOT.get();
    }

}