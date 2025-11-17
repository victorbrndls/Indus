package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.crafting.IndusRecipeHelper;
import com.victorbrndls.indus.crafting.IndusRecipes;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class Assembler1BlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos INPUT_1_POS = new BlockPos(2, 0, -5);
    private final static BlockPos INPUT_2_POS = new BlockPos(3, 0, -5);
    private final static BlockPos INPUT_3_POS = new BlockPos(4, 0, -5);
    private final static BlockPos INPUT_4_POS = new BlockPos(5, 0, -5);
    private final static BlockPos OUTPUT_POS = new BlockPos(5, 0, 0);

    public Assembler1BlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.ASSEMBLER_1.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.ASSEMBLER_1;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        if ((level.getGameTime() % 80) != 0) return;

        var input1Handler = getRelativeItemHandler(level, INPUT_1_POS);
        var input2Handler = getRelativeItemHandler(level, INPUT_2_POS);
        var input3Handler = getRelativeItemHandler(level, INPUT_3_POS);
        var input4Handler = getRelativeItemHandler(level, INPUT_4_POS);
        var outputHandler = getRelativeItemHandler(level, OUTPUT_POS);

        if (
                input1Handler == null || input2Handler == null || input3Handler == null || input4Handler == null ||
                        outputHandler == null
        ) return;

        var recipe = IndusRecipeHelper.getRecipe(
                (ServerLevel) level,
                IndusRecipes.ASSEMBLER_RECIPE_TYPE.get(),
                input1Handler,
                input2Handler,
                input3Handler,
                input4Handler
        );
        if (recipe == null) return;

        var crafted = IndusRecipeHelper.craftRecipe(
                recipe,
                outputHandler,
                input1Handler,
                input2Handler,
                input3Handler,
                input4Handler
        );
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.assembler_1");
    }
}