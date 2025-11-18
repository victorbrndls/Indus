package com.victorbrndls.indus.integration.jei.category;

import com.victorbrndls.indus.blocks.IndusBlocks;
import com.victorbrndls.indus.crafting.CrusherRecipe;
import com.victorbrndls.indus.integration.jei.Base4Category;
import com.victorbrndls.indus.integration.jei.JEIPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class CrusherCategory extends Base4Category<CrusherRecipe> {

    public CrusherCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(IndusBlocks.CRUSHER.get());
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.indus.crusher");
    }

    @Override
    public RecipeType<CrusherRecipe> getRecipeType() {
        return JEIPlugin.CRUSHER_CATEGORY;
    }
}
