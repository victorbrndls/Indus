package com.victorbrndls.indus.integration.jei;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.crafting.BaseCustomRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class Base7Category<T extends BaseCustomRecipe> implements IRecipeCategory<T> {

    protected static final ResourceLocation BACKGROUND = Indus.rl("textures/gui/jei_basic_7.png");

    protected final IGuiHelper helper;
    protected final IDrawableStatic background;

    public Base7Category(IGuiHelper helper) {
        this.helper = helper;
        background = helper.createDrawable(BACKGROUND, 0, 0, getWidth(), getHeight());
    }

    @Override
    public void draw(BaseCustomRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BaseCustomRecipe recipe, IFocusGroup focuses) {
        var i1 = builder.addSlot(RecipeIngredientRole.INPUT, 1, 1);
        var i2 = builder.addSlot(RecipeIngredientRole.INPUT, 19, 1);
        var i3 = builder.addSlot(RecipeIngredientRole.INPUT, 37, 1);
        var i4 = builder.addSlot(RecipeIngredientRole.INPUT, 55, 1);
        var i5 = builder.addSlot(RecipeIngredientRole.INPUT, 73, 1);
        var i6 = builder.addSlot(RecipeIngredientRole.INPUT, 91, 1);
        var i7 = builder.addSlot(RecipeIngredientRole.INPUT, 109, 1);
        var o1 = builder.addSlot(RecipeIngredientRole.OUTPUT, 142, 1);

        var ingredients = recipe.inputs();

        if (ingredients.size() > 0) {
            i1.addItemStack(ingredients.get(0).ingredient().getItems()[0].copyWithCount(ingredients.get(0).count()));
        }
        if (ingredients.size() > 1) {
            i2.addItemStack(ingredients.get(1).ingredient().getItems()[0].copyWithCount(ingredients.get(1).count()));
        }
        if (ingredients.size() > 2) {
            i3.addItemStack(ingredients.get(2).ingredient().getItems()[0].copyWithCount(ingredients.get(2).count()));
        }
        if (ingredients.size() > 3) {
            i4.addItemStack(ingredients.get(3).ingredient().getItems()[0].copyWithCount(ingredients.get(3).count()));
        }
        if (ingredients.size() > 4) {
            i5.addItemStack(ingredients.get(4).ingredient().getItems()[0].copyWithCount(ingredients.get(4).count()));
        }
        if (ingredients.size() > 5) {
            i6.addItemStack(ingredients.get(5).ingredient().getItems()[0].copyWithCount(ingredients.get(5).count()));
        }
        if (ingredients.size() > 6) {
            i7.addItemStack(ingredients.get(6).ingredient().getItems()[0].copyWithCount(ingredients.get(6).count()));
        }

        o1.addItemStack(recipe.result());
    }

    @Override
    public int getWidth() {
        return 159;
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, icon());
    }

    public abstract ItemStack icon();

    @Override
    public abstract Component getTitle();

}
