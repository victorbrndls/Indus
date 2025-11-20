package com.victorbrndls.indus.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public final class BlastFurnaceRecipe extends BaseCustomRecipe {

    public static final MapCodec<BlastFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ExtraCodecs.nonEmptyList(CountedIngredient.CODEC.listOf()).fieldOf("inputs").forGetter(BlastFurnaceRecipe::inputs),
            ItemStack.CODEC.fieldOf("result").forGetter(BlastFurnaceRecipe::result)
    ).apply(inst, BlastFurnaceRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, List<CountedIngredient>> INPUTS_STREAM =
            CountedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list());

    public static final StreamCodec<RegistryFriendlyByteBuf, BlastFurnaceRecipe> STREAM_CODEC = StreamCodec.composite(
            INPUTS_STREAM, BlastFurnaceRecipe::inputs,
            ItemStack.STREAM_CODEC, BlastFurnaceRecipe::result,
            BlastFurnaceRecipe::new
    );

    public BlastFurnaceRecipe(List<CountedIngredient> inputs, ItemStack result) {
        super(inputs, result);
    }

    @Override
    public RecipeType<? extends Recipe<CraftingInput>> getType() {
        return IndusRecipes.BLAST_FURNACE_RECIPE_TYPE.get();
    }

    @Override
    public RecipeSerializer<BlastFurnaceRecipe> getSerializer() {
        return IndusRecipes.BLAST_FURNACE_RECIPE_SERIALIZER.value();
    }
}
