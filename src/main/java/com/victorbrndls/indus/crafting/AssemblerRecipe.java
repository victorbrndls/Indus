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

public final class AssemblerRecipe extends BaseCustomRecipe {

    public static final MapCodec<AssemblerRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ExtraCodecs.nonEmptyList(CountedIngredient.CODEC.listOf()).fieldOf("inputs").forGetter(AssemblerRecipe::inputs),
            ItemStack.CODEC.fieldOf("result").forGetter(AssemblerRecipe::result)
    ).apply(inst, AssemblerRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, List<CountedIngredient>> INPUTS_STREAM =
            CountedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list());

    public static final StreamCodec<RegistryFriendlyByteBuf, AssemblerRecipe> STREAM_CODEC = StreamCodec.composite(
            INPUTS_STREAM, AssemblerRecipe::inputs,
            ItemStack.STREAM_CODEC, AssemblerRecipe::result,
            AssemblerRecipe::new
    );

    public AssemblerRecipe(List<CountedIngredient> inputs, ItemStack result) {
        super(inputs, result);
    }

    @Override
    public RecipeType<? extends Recipe<CraftingInput>> getType() {
        return IndusRecipes.ASSEMBLER_RECIPE_TYPE.get();
    }

    @Override
    public RecipeSerializer<AssemblerRecipe> getSerializer() {
        return IndusRecipes.ASSEMBLER_RECIPE_SERIALIZER.value();
    }
}
