package com.victorbrndls.indus.crafting;

import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndusRecipeCacheClient {

    private static final Map<RecipeType<?>, List<? extends Recipe<?>>> CACHE = new HashMap<>();

    public static void onRecipesReceived(RecipesReceivedEvent event) {
        addRecipes(event.getRecipeMap(), IndusRecipes.MIXER_RECIPE_TYPE.get());
        addRecipes(event.getRecipeMap(), IndusRecipes.CRUSHER_RECIPE_TYPE.get());
        addRecipes(event.getRecipeMap(), IndusRecipes.ASSEMBLER_RECIPE_TYPE.get());
        addRecipes(event.getRecipeMap(), IndusRecipes.MAINTENANCE_DEPOT_RECIPE_TYPE.get());
    }

    private static <I extends RecipeInput, T extends Recipe<I>> void addRecipes(RecipeMap recipeMap, RecipeType<T> type) {
        CACHE.put(
                type,
                recipeMap.byType(type).stream()
                        .map(RecipeHolder::value)
                        .toList()
        );
    }

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> List<T> getRecipes(RecipeType<T> type) {
        return (List<T>) CACHE.get(type);
    }

}
