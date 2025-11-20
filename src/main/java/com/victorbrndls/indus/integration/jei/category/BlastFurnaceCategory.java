package com.victorbrndls.indus.integration.jei.category;

import com.victorbrndls.indus.blocks.IndusBlocks;
import com.victorbrndls.indus.crafting.BlastFurnaceRecipe;
import com.victorbrndls.indus.integration.jei.Base4Category;
import com.victorbrndls.indus.integration.jei.JEIPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class BlastFurnaceCategory extends Base4Category<BlastFurnaceRecipe> {

    public BlastFurnaceCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(IndusBlocks.BLAST_FURNACE.get());
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.indus.blast_furnace");
    }

    @Override
    public RecipeType<BlastFurnaceRecipe> getRecipeType() {
        return JEIPlugin.BLAST_FURNACE_CATEGORY;
    }
}
