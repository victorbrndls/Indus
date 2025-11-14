package com.victorbrndls.indus.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class BaseCustomRecipe implements Recipe<CraftingInput> {

    protected final List<CountedIngredient> inputs;
    protected final ItemStack result;

    public BaseCustomRecipe(List<CountedIngredient> inputs, ItemStack result) {
        this.inputs = List.copyOf(inputs);
        this.result = result.copy();
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    public List<CountedIngredient> inputs() {
        return inputs;
    }

    public ItemStack result() {
        return result.copy();
    }

}
