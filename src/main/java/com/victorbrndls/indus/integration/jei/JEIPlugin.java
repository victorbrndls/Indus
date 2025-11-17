package com.victorbrndls.indus.integration.jei;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.blocks.IndusBlocks;
import com.victorbrndls.indus.crafting.CrusherRecipe;
import com.victorbrndls.indus.crafting.IndusRecipeCacheClient;
import com.victorbrndls.indus.crafting.IndusRecipes;
import com.victorbrndls.indus.crafting.MixerRecipe;
import com.victorbrndls.indus.integration.jei.category.CrusherCategory;
import com.victorbrndls.indus.integration.jei.category.MixerCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    public static final IRecipeType<MixerRecipe> MIXER_CATEGORY = IRecipeType.create(Indus.MODID, "mixer", MixerRecipe.class);
    public static final IRecipeType<CrusherRecipe> CRUSHER_CATEGORY = IRecipeType.create(Indus.MODID, "crusher", CrusherRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return Indus.rl(Indus.MODID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(MIXER_CATEGORY, new ItemStack(IndusBlocks.MIXER.get()));
        registration.addCraftingStation(CRUSHER_CATEGORY, new ItemStack(IndusBlocks.CRUSHER.get()));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MixerCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CrusherCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(
                MIXER_CATEGORY,
                IndusRecipeCacheClient.getRecipes(IndusRecipes.MIXER_RECIPE_TYPE.get())
        );
        registration.addRecipes(
                CRUSHER_CATEGORY,
                IndusRecipeCacheClient.getRecipes(IndusRecipes.CRUSHER_RECIPE_TYPE.get())
        );
    }

}
