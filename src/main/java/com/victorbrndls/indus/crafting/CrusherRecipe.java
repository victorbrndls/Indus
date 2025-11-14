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

public final class CrusherRecipe extends BaseCustomRecipe {

    public static final MapCodec<CrusherRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ExtraCodecs.nonEmptyList(CountedIngredient.CODEC.listOf()).fieldOf("inputs").forGetter(CrusherRecipe::inputs),
            ItemStack.CODEC.fieldOf("result").forGetter(CrusherRecipe::result)
    ).apply(inst, CrusherRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, List<CountedIngredient>> INPUTS_STREAM =
            CountedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list());

    public static final StreamCodec<RegistryFriendlyByteBuf, CrusherRecipe> STREAM_CODEC = StreamCodec.composite(
            INPUTS_STREAM, CrusherRecipe::inputs,
            ItemStack.STREAM_CODEC, CrusherRecipe::result,
            CrusherRecipe::new
    );

    public CrusherRecipe(List<CountedIngredient> inputs, ItemStack result) {
        super(inputs, result);
    }

    @Override
    public RecipeType<? extends Recipe<CraftingInput>> getType() {
        return IndusRecipes.CRUSHER_RECIPE_TYPE.get();
    }

    @Override
    public RecipeSerializer<CrusherRecipe> getSerializer() {
        return IndusRecipes.CRUSHER_RECIPE_SERIALIZER.value();
    }
}
