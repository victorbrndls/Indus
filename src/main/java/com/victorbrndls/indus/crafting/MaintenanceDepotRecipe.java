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

public final class MaintenanceDepotRecipe extends BaseCustomRecipe {

    public static final MapCodec<MaintenanceDepotRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ExtraCodecs.nonEmptyList(CountedIngredient.CODEC.listOf()).fieldOf("inputs").forGetter(MaintenanceDepotRecipe::inputs),
            ItemStack.CODEC.fieldOf("result").forGetter(MaintenanceDepotRecipe::result)
    ).apply(inst, MaintenanceDepotRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, List<CountedIngredient>> INPUTS_STREAM =
            CountedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list());

    public static final StreamCodec<RegistryFriendlyByteBuf, MaintenanceDepotRecipe> STREAM_CODEC = StreamCodec.composite(
            INPUTS_STREAM, MaintenanceDepotRecipe::inputs,
            ItemStack.STREAM_CODEC, MaintenanceDepotRecipe::result,
            MaintenanceDepotRecipe::new
    );

    public MaintenanceDepotRecipe(List<CountedIngredient> inputs, ItemStack result) {
        super(inputs, result);
    }

    @Override
    public RecipeType<? extends Recipe<CraftingInput>> getType() {
        return IndusRecipes.MAINTENANCE_DEPOT_RECIPE_TYPE.get();
    }

    @Override
    public RecipeSerializer<MaintenanceDepotRecipe> getSerializer() {
        return IndusRecipes.MAINTENANCE_DEPOT_RECIPE_SERIALIZER.value();
    }
}
