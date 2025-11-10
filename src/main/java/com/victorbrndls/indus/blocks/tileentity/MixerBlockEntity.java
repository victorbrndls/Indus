package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.crafting.IndusRecipes;
import com.victorbrndls.indus.crafting.MixerRecipe;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.shared.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class MixerBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos INPUT_1_POS = new BlockPos(2, 1, -5);
    private final static BlockPos INPUT_2_POS = new BlockPos(3, 2, -5);
    private final static BlockPos INPUT_3_POS = new BlockPos(4, 3, -5);
    private final static BlockPos OUTPUT_POS = new BlockPos(4, 1, 0);

    public MixerBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.MIXER.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.MIXER;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        tickCounter++;

        if (tickCounter >= 20) {
            tickCounter = 0;

            BlockPos input1Pos = BlockHelper.offsetFrontFacing(pos, state, INPUT_1_POS);
            BlockPos input2Pos = BlockHelper.offsetFrontFacing(pos, state, INPUT_2_POS);
            BlockPos input3Pos = BlockHelper.offsetFrontFacing(pos, state, INPUT_3_POS);
            BlockPos outputPos = BlockHelper.offsetFrontFacing(pos, state, OUTPUT_POS);

            ResourceHandler<ItemResource> input1Handler = BlockHelper.getItemHandlerAt(level, input1Pos);
            ResourceHandler<ItemResource> input2Handler = BlockHelper.getItemHandlerAt(level, input2Pos);
            ResourceHandler<ItemResource> input3Handler = BlockHelper.getItemHandlerAt(level, input3Pos);
            ResourceHandler<ItemResource> outputHandler = BlockHelper.getItemHandlerAt(level, outputPos);

            if (input1Handler == null || input2Handler == null || input3Handler == null) return;
            if (outputHandler == null) return;

            var recipes = ((ServerLevel) level).recipeAccess().getRecipes().stream()
                    .filter(r -> r.value().getType() == IndusRecipes.MIXER_RECIPE_TYPE.get())
                    .map(r -> (MixerRecipe) r.value())
                    .toList();

            var matchingRecipe = recipes.stream()
                    .filter(r -> canCraftOnce(r, input1Handler, input2Handler, input3Handler))
                    .findFirst();
            if (matchingRecipe.isEmpty()) return;

            MixerRecipe recipe = matchingRecipe.get();

            try (var tx = Transaction.open(null)) {
                for (var need : recipe.inputs()) {
                    int rem = need.count();
                    rem = drain(input1Handler, need.ingredient(), rem, tx);
                    rem = drain(input2Handler, need.ingredient(), rem, tx);
                    rem = drain(input3Handler, need.ingredient(), rem, tx);
                    if (rem > 0) {
                        tx.close();
                        return;
                    }
                }

                if (!insertAll(outputHandler, recipe.result(), tx)) {
                    tx.close();
                    return;
                }

                tx.commit();
                setChanged();
            }
        }
    }

    private static int available(ResourceHandler<ItemResource> h, Ingredient ing) {
        if (h == null) return 0;
        int total = 0;
        for (int i = 0, n = h.size(); i < n; i++) {
            ItemResource res = h.getResource(i);
            if (res == null) continue;
            if (!ing.test(new ItemStack(res.getItem()))) continue;
            long amt = h.getAmountAsLong(i);
            if (amt > 0) total += (int) Math.min(amt, Integer.MAX_VALUE);
        }
        return total;
    }

    @SafeVarargs
    private static boolean canCraftOnce(MixerRecipe r, ResourceHandler<ItemResource>... hs) {
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

    private static int drain(ResourceHandler<ItemResource> h, Ingredient ing, int need, TransactionContext tx) {
        if (h == null || need <= 0) return need;
        // drain per slot to avoid extracting wrong items
        for (int i = 0, n = h.size(); i < n && need > 0; i++) {
            ItemResource res = h.getResource(i);
            if (res == null) continue;
            if (!ing.test(new ItemStack(res.getItem()))) continue;
            long avail = h.getAmountAsLong(i);
            if (avail <= 0) continue;
            int take = Math.min(need, (int) Math.min(avail, Integer.MAX_VALUE));
            if (take > 0) need -= h.extract(i, res, take, tx);
        }
        return need;
    }

    private static boolean insertAll(ResourceHandler<ItemResource> out, ItemStack stack, TransactionContext tx) {
        return out.insert(ItemResource.of(stack), stack.getCount(), tx) == stack.getCount();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.mixer");
    }
}