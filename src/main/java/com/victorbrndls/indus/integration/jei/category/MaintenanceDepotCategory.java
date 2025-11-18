package com.victorbrndls.indus.integration.jei.category;

import com.victorbrndls.indus.blocks.IndusBlocks;
import com.victorbrndls.indus.crafting.MaintenanceDepotRecipe;
import com.victorbrndls.indus.integration.jei.Base4Category;
import com.victorbrndls.indus.integration.jei.JEIPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class MaintenanceDepotCategory extends Base4Category<MaintenanceDepotRecipe> {

    public MaintenanceDepotCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(IndusBlocks.MAINTENANCE_DEPOT.get());
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.indus.maintenance_depot");
    }

    @Override
    public RecipeType<MaintenanceDepotRecipe> getRecipeType() {
        return JEIPlugin.MAINTENANCE_DEPOT_CATEGORY;
    }
}
