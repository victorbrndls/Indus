package com.victorbrndls.indus.integration.jei;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.blocks.IndusBlocks;
import com.victorbrndls.indus.crafting.*;
import com.victorbrndls.indus.integration.jei.category.*;
import com.victorbrndls.indus.mod.structure.IndusStructureRequirements;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    public static final RecipeType<BlastFurnaceRecipe> BLAST_FURNACE_CATEGORY = RecipeType.create(Indus.MODID, "blast_furnace", BlastFurnaceRecipe.class);
    public static final RecipeType<MixerRecipe> MIXER_CATEGORY = RecipeType.create(Indus.MODID, "mixer", MixerRecipe.class);
    public static final RecipeType<CrusherRecipe> CRUSHER_CATEGORY = RecipeType.create(Indus.MODID, "crusher", CrusherRecipe.class);
    public static final RecipeType<AssemblerRecipe> ASSEMBLER_CATEGORY = RecipeType.create(Indus.MODID, "assembler", AssemblerRecipe.class);
    public static final RecipeType<MaintenanceDepotRecipe> MAINTENANCE_DEPOT_CATEGORY = RecipeType.create(Indus.MODID, "maintenance_depot", MaintenanceDepotRecipe.class);
    public static final RecipeType<AssemblerRecipe> STRUCTURE_REQUIREMENTS_CATEGORY = RecipeType.create(Indus.MODID, "structure_requirements", AssemblerRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return Indus.rl(Indus.MODID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalysts(BLAST_FURNACE_CATEGORY, new ItemStack(IndusBlocks.BLAST_FURNACE.get()));
        registration.addRecipeCatalysts(MIXER_CATEGORY, new ItemStack(IndusBlocks.MIXER.get()));
        registration.addRecipeCatalysts(CRUSHER_CATEGORY, new ItemStack(IndusBlocks.CRUSHER.get()));
        registration.addRecipeCatalysts(ASSEMBLER_CATEGORY, new ItemStack(IndusBlocks.ASSEMBLER_1.get()));
        registration.addRecipeCatalysts(MAINTENANCE_DEPOT_CATEGORY, new ItemStack(IndusBlocks.MAINTENANCE_DEPOT.get()));
//        registration.addRecipeCatalysts(STRUCTURE_REQUIREMENTS_CATEGORY, new ItemStack(IndusItems.WRENCH.get()));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new BlastFurnaceCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new MixerCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CrusherCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AssemblerCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new MaintenanceDepotCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new StructureRequirementsCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(
                BLAST_FURNACE_CATEGORY,
                IndusRecipeCacheClient.getRecipes(IndusRecipes.BLAST_FURNACE_RECIPE_TYPE.get())
        );
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
        registration.addRecipes(
                STRUCTURE_REQUIREMENTS_CATEGORY,
                IndusStructureRequirements.getRecipes()
        );
    }

}
