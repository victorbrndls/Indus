package com.victorbrndls.indus.integration.jei;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.blocks.IndusBlocks;
import com.victorbrndls.indus.crafting.*;
import com.victorbrndls.indus.integration.jei.category.AssemblerCategory;
import com.victorbrndls.indus.integration.jei.category.CrusherCategory;
import com.victorbrndls.indus.integration.jei.category.MaintenanceDepotCategory;
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
    public static final IRecipeType<AssemblerRecipe> ASSEMBLER_CATEGORY = IRecipeType.create(Indus.MODID, "assembler", AssemblerRecipe.class);
    public static final IRecipeType<MaintenanceDepotRecipe> MAINTENANCE_DEPOT_CATEGORY = IRecipeType.create(Indus.MODID, "maintenance_depot", MaintenanceDepotRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return Indus.rl(Indus.MODID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(MIXER_CATEGORY, new ItemStack(IndusBlocks.MIXER.get()));
        registration.addCraftingStation(CRUSHER_CATEGORY, new ItemStack(IndusBlocks.CRUSHER.get()));
        registration.addCraftingStation(ASSEMBLER_CATEGORY, new ItemStack(IndusBlocks.ASSEMBLER_1.get()));
        registration.addCraftingStation(MAINTENANCE_DEPOT_CATEGORY, new ItemStack(IndusBlocks.MAINTENANCE_DEPOT.get()));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MixerCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CrusherCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AssemblerCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new MaintenanceDepotCategory(registration.getJeiHelpers().getGuiHelper()));
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
        registration.addRecipes(
                ASSEMBLER_CATEGORY,
                IndusRecipeCacheClient.getRecipes(IndusRecipes.ASSEMBLER_RECIPE_TYPE.get())
        );
        registration.addRecipes(
                MAINTENANCE_DEPOT_CATEGORY,
                IndusRecipeCacheClient.getRecipes(IndusRecipes.MAINTENANCE_DEPOT_RECIPE_TYPE.get())
        );
    }

}
