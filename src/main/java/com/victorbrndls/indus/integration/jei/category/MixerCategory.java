package com.victorbrndls.indus.integration.jei.category;

import com.victorbrndls.indus.blocks.IndusBlocks;
import com.victorbrndls.indus.crafting.MixerRecipe;
import com.victorbrndls.indus.integration.jei.Base4Category;
import com.victorbrndls.indus.integration.jei.JEIPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class MixerCategory extends Base4Category<MixerRecipe> {

    public MixerCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(IndusBlocks.MIXER.get());
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.indus.mixer");
    }

    @Override
    public RecipeType<MixerRecipe> getRecipeType() {
        return JEIPlugin.MIXER_CATEGORY;
    }
}
