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

public class CrusherBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos INPUT_POS = new BlockPos(2, 1, -5);
    private final static BlockPos OUTPUT_POS = new BlockPos(4, 1, 0);

    public CrusherBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.CRUSHER.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.CRUSHER;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        tickCounter++;

        if (tickCounter >= 80) {
            tickCounter = 0;

            ResourceHandler<ItemResource> inputHandler = getRelativeItemHandler(level, INPUT_POS);
            ResourceHandler<ItemResource> outputHandler = getRelativeItemHandler(level, OUTPUT_POS);

            if (inputHandler == null || outputHandler == null) return;

            var recipe = IndusRecipeHelper.getRecipe(
                    (ServerLevel) level,
                    IndusRecipes.MIXER_RECIPE_TYPE.get(),
                    inputHandler
            );
            if (recipe == null) return;

            var crafted = IndusRecipeHelper.craftRecipe(
                    recipe,
                    outputHandler,
                    inputHandler
            );
        }
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.crusher");
    }
}