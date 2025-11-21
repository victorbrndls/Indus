package com.victorbrndls.indus.integration.jei.category;

import com.victorbrndls.indus.crafting.AssemblerRecipe;
import com.victorbrndls.indus.integration.jei.Base4Category;
import com.victorbrndls.indus.integration.jei.Base7Category;
import com.victorbrndls.indus.integration.jei.JEIPlugin;
import com.victorbrndls.indus.items.IndusItems;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class StructureRequirementsCategory extends Base7Category<AssemblerRecipe> {

    public StructureRequirementsCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(IndusItems.WRENCH.get());
    }

    @Override
    public Component getTitle() {
        return Component.literal("Structure Requirements");
    }

    @Override
    public RecipeType<AssemblerRecipe> getRecipeType() {
        return JEIPlugin.STRUCTURE_REQUIREMENTS_CATEGORY;
    }
}
