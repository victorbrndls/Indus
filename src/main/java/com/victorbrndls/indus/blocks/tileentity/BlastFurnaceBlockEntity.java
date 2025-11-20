package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.crafting.IndusRecipeHelper;
import com.victorbrndls.indus.crafting.IndusRecipes;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlastFurnaceBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos INPUT_1_POS = new BlockPos(4, 2, -5);
    private final static BlockPos INPUT_2_POS = new BlockPos(1, 1, -5);
    private final static BlockPos OUTPUT_POS = new BlockPos(3, 1, 0);

    public BlastFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.BLAST_FURNACE.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.BLAST_FURNACE;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        if ((level.getGameTime() % 80) != 0) return;

        var input1Handler = getRelativeItemHandler(level, INPUT_1_POS);
        var input2Handler = getRelativeItemHandler(level, INPUT_2_POS);
        var outputHandler = getRelativeItemHandler(level, OUTPUT_POS);

        if (input1Handler == null || input2Handler == null || outputHandler == null) return;

        var recipe = IndusRecipeHelper.getRecipe(
                (ServerLevel) level,
                IndusRecipes.BLAST_FURNACE_RECIPE_TYPE.get(),
                input1Handler,
                input2Handler
        );
        if (recipe == null) return;

        var fits = IndusRecipeHelper.fits(outputHandler, recipe);
        if (!fits) return;

        IndusRecipeHelper.craftRecipe(
                recipe,
                outputHandler,
                input1Handler,
                input2Handler
        );
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.blast_furnace");
    }
}