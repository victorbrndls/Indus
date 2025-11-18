package com.victorbrndls.indus.crafting;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndusRecipeHelper {

    private static final Map<RecipeType<?>, List<? extends Recipe<?>>> CACHE = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> List<T> getRecipes(ServerLevel level, RecipeType<T> type) {
        return (List<T>) CACHE.computeIfAbsent(type, t ->
                level.recipeAccess().getRecipes().stream()
                        .map(RecipeHolder::value)
                        .filter(r -> r.getType() == t)
                        .toList()
        );
    }

    @SafeVarargs
    public static <T extends Recipe<?>> T getRecipe(
            ServerLevel level, RecipeType<T> type, ResourceHandler<ItemResource>... hs) {
        return getRecipes(level, type).stream()
                .filter(r -> canCraftOnce((BaseCustomRecipe) r, hs))
                .findFirst().orElse(null);
    }

    @SafeVarargs
    public static boolean craftRecipe(
            BaseCustomRecipe recipe,
            ResourceHandler<ItemResource> output,
            ResourceHandler<ItemResource>... inputs
    ) {
        try (var tx = Transaction.open(null)) {
            for (var need : recipe.inputs()) {
                int rem = need.count();

                for (var input : inputs) {
                    rem = IndusRecipeHelper.drain(input, need.ingredient(), rem, tx);
                    if (rem <= 0) break;
                }

                if (rem > 0) {
                    tx.close();
                    return false;
                }
            }

            if (!IndusRecipeHelper.insertAll(output, recipe.result(), tx)) {
                tx.close();
                return false;
            }

            tx.commit();
            return true;
        }
    }

    private static int available(ResourceHandler<ItemResource> h, Ingredient ing) {
        if (h == null) return 0;
        int total = 0;
        for (int i = 0, n = h.size(); i < n; i++) {
            ItemResource res = h.getResource(i);
            if (!ing.test(new ItemStack(res.getItem()))) continue;
            long amt = h.getAmountAsLong(i);
            if (amt > 0) total += (int) Math.min(amt, Integer.MAX_VALUE);
        }
        return total;
    }

    @SafeVarargs
    private static boolean canCraftOnce(BaseCustomRecipe r, ResourceHandler<ItemResource>... hs) {
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

    public static int drain(ResourceHandler<ItemResource> h, Ingredient ing, int need, TransactionContext tx) {
        if (h == null || need <= 0) return need;
        // drain per slot to avoid extracting wrong items
        for (int i = 0, n = h.size(); i < n && need > 0; i++) {
            ItemResource res = h.getResource(i);
            if (!ing.test(new ItemStack(res.getItem()))) continue;
            long avail = h.getAmountAsLong(i);
            if (avail <= 0) continue;
            int take = Math.min(need, (int) Math.min(avail, Integer.MAX_VALUE));
            need -= h.extract(i, res, take, tx);
        }
        return need;
    }

    public static boolean fits(ResourceHandler<ItemResource> out, BaseCustomRecipe r) {
        try (var tx = Transaction.open(null)) {
            ItemStack result = r.result();
            return out.insert(ItemResource.of(result), result.getCount(), tx) > 0;
        }
    }

    public static boolean insertAll(ResourceHandler<ItemResource> out, ItemStack stack, TransactionContext tx) {
        return out.insert(ItemResource.of(stack), stack.getCount(), tx) == stack.getCount();
    }

}
