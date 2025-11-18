package com.victorbrndls.indus.crafting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.crafting.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndusRecipeCacheClient {

    private static final Map<RecipeType<?>, List<? extends Recipe<?>>> CACHE = new HashMap<>();

    private static boolean initialized = false;

    private static void ensureLoaded() {
        if (initialized) return;

        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) return; // not in a world yet

        RecipeManager manager = level.getRecipeManager();

        addRecipes(manager, IndusRecipes.MIXER_RECIPE_TYPE.get());
        addRecipes(manager, IndusRecipes.CRUSHER_RECIPE_TYPE.get());
        addRecipes(manager, IndusRecipes.ASSEMBLER_RECIPE_TYPE.get());
        addRecipes(manager, IndusRecipes.MAINTENANCE_DEPOT_RECIPE_TYPE.get());

        initialized = true;
    }

    private static <I extends RecipeInput, T extends Recipe<I>> void addRecipes(RecipeManager manager, RecipeType<T> type) {
        CACHE.put(
                type,
                manager.getAllRecipesFor(type).stream()
                        .map(RecipeHolder::value)
                        .toList()
        );
    }

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> List<T> getRecipes(RecipeType<T> type) {
        ensureLoaded();
        return (List<T>) CACHE.getOrDefault(type, List.of());
    }

}
