package com.victorbrndls.indus.crafting;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndusRecipeHelper {

    private static final Map<RecipeType<?>, List<? extends Recipe<?>>> CACHE = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> List<T> getRecipes(ServerLevel level, RecipeType<T> type) {
        return (List<T>) CACHE.computeIfAbsent(type, t ->
                level.getRecipeManager()
                        .getRecipes()
                        .stream()
                        .map(RecipeHolder::value)
                        .filter(r -> r.getType() == t)
                        .toList()
        );
    }

    public static <T extends Recipe<?>> T getRecipe(
            ServerLevel level, RecipeType<T> type, IItemHandler... hs
    ) {
        return getRecipes(level, type).stream()
                .filter(r -> canCraftOnce((BaseCustomRecipe) r, hs))
                .findFirst()
                .orElse(null);
    }

    public static boolean craftRecipe(
            BaseCustomRecipe recipe,
            IItemHandler output,
            IItemHandler... inputs
    ) {
        // First: verify we have enough inputs
        if (!canCraftOnce(recipe, inputs)) return false;

        // Second: verify output has space
        if (!fits(output, recipe)) return false;

        // Third: actually consume inputs
        for (var need : recipe.inputs()) {
            int rem = need.count();

            for (var input : inputs) {
                rem = drain(input, need.ingredient(), rem);
                if (rem <= 0) break;
            }

            if (rem > 0) {
                // Should not happen after canCraftOnce, but if it does, we abort
                return false;
            }
        }

        // Finally: insert result
        ItemStack result = recipe.result().copy();
        ItemStack leftover = ItemHandlerHelper.insertItem(output, result, false);
        return leftover.isEmpty();
    }

    private static int available(IItemHandler h, Ingredient ing) {
        if (h == null) return 0;
        int total = 0;
        int n = h.getSlots();
        for (int i = 0; i < n; i++) {
            ItemStack stack = h.getStackInSlot(i);
            if (stack.isEmpty() || !ing.test(stack)) continue;
            total += stack.getCount();
        }
        return total;
    }

    private static boolean canCraftOnce(BaseCustomRecipe r, IItemHandler... hs) {
        for (var need : r.inputs()) {
            int have = 0;
            for (var h : hs) {
                have += available(h, need.ingredient());
                if (have >= need.count()) break;
            }
            if (have < need.count()) return false;
        }
        return true;
    }

    /**
     * Drain up to {@code need} items matching {@code ing} from handler {@code h}.
     * Returns remaining amount still needed (0 means fully drained).
     */
    public static int drain(IItemHandler h, Ingredient ing, int need) {
        if (h == null || need <= 0) return need;

        int n = h.getSlots();
        for (int i = 0; i < n && need > 0; i++) {
            ItemStack stack = h.getStackInSlot(i);
            if (stack.isEmpty() || !ing.test(stack)) continue;

            int toExtract = Math.min(stack.getCount(), need);
            ItemStack extracted = h.extractItem(i, toExtract, false);
            if (!extracted.isEmpty()) {
                need -= extracted.getCount();
            }
        }
        return need;
    }

    public static boolean fits(IItemHandler out, BaseCustomRecipe r) {
        ItemStack result = r.result().copy();
        ItemStack remainder = ItemHandlerHelper.insertItem(out, result, true); // simulate
        return remainder.isEmpty();
    }

}
