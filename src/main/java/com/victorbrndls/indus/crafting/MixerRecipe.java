package com.victorbrndls.indus.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public final class MixerRecipe implements Recipe<CraftingInput> {

    public static final MapCodec<MixerRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ExtraCodecs.nonEmptyList(CountedIngredient.CODEC.listOf()).fieldOf("inputs").forGetter(MixerRecipe::inputs),
            ItemStack.CODEC.fieldOf("result").forGetter(MixerRecipe::result)
    ).apply(inst, MixerRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, List<CountedIngredient>> INPUTS_STREAM =
            CountedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list());

    public static final StreamCodec<RegistryFriendlyByteBuf, MixerRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    INPUTS_STREAM, MixerRecipe::inputs,
                    ItemStack.STREAM_CODEC, MixerRecipe::result,
                    MixerRecipe::new
            );

    private final List<CountedIngredient> inputs;
    private final ItemStack result;

    public MixerRecipe(List<CountedIngredient> inputs, ItemStack result) {
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

    @Override
    public RecipeType<? extends Recipe<CraftingInput>> getType() {
        return IndusRecipes.MIXER_RECIPE_TYPE.get();
    }

    @Override
    public RecipeSerializer<MixerRecipe> getSerializer() {
        return IndusRecipes.MIXER_RECIPE_SERIALIZER.value();
    }
}
