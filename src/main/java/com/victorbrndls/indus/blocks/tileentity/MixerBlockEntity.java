package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.crafting.IndusRecipeHelper;
import com.victorbrndls.indus.crafting.IndusRecipes;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.mod.structure.IndusStructureStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MixerBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos INPUT_1_POS = new BlockPos(2, 1, -5);
    private final static BlockPos INPUT_2_POS = new BlockPos(3, 1, -5);
    private final static BlockPos INPUT_3_POS = new BlockPos(4, 1, -5);
    private final static BlockPos OUTPUT_POS = new BlockPos(4, 1, 0);

    public MixerBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.MIXER.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.MIXER;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        if ((level.getGameTime() % 80) != 0) return;

        var input1Handler = getRelativeItemHandler(level, INPUT_1_POS);
        var input2Handler = getRelativeItemHandler(level, INPUT_2_POS);
        var input3Handler = getRelativeItemHandler(level, INPUT_3_POS);
        var outputHandler = getRelativeItemHandler(level, OUTPUT_POS);

        if (
                input1Handler == null || input2Handler == null || input3Handler == null || outputHandler == null
        ) {
            setStatus(IndusStructureStatus.INVALID_STRUCTURE);
            return;
        }

        var recipe = IndusRecipeHelper.getRecipe(
                (ServerLevel) level,
                IndusRecipes.MIXER_RECIPE_TYPE.get(),
                input1Handler,
                input2Handler,
                input3Handler
        );
        if (recipe == null) {
            setStatus(IndusStructureStatus.IDLE);
            return;
        }

        var crafted = IndusRecipeHelper.craftRecipe(
                recipe,
                outputHandler,
                input1Handler,
                input2Handler,
                input3Handler
        );

        if (crafted) {
            setStatus(IndusStructureStatus.WORKING);
        } else {
            setStatus(IndusStructureStatus.OUTPUT_FULL);
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.mixer");
    }
}