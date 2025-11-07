package com.victorbrndls.indus.mod.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public record IndusStructureInfo(IndusStructure structure, List<BlockPos> pos, List<BlockState> blockState) {
}
