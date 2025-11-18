package com.victorbrndls.indus.integration.jei.category;

import com.victorbrndls.indus.blocks.IndusBlocks;
import com.victorbrndls.indus.crafting.AssemblerRecipe;
import com.victorbrndls.indus.integration.jei.Base4Category;
import com.victorbrndls.indus.integration.jei.JEIPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class AssemblerCategory extends Base4Category<AssemblerRecipe> {

    public AssemblerCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(IndusBlocks.ASSEMBLER_1.get());
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.indus.assembler_1");
    }

    @Override
    public RecipeType<AssemblerRecipe> getRecipeType() {
        return JEIPlugin.ASSEMBLER_CATEGORY;
    }
}
