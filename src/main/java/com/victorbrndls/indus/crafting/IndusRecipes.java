package com.victorbrndls.indus.crafting;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.crafting.data.DeferredRecipeSerializer;
import com.victorbrndls.indus.crafting.data.DeferredRecipeSerializerRegister;
import com.victorbrndls.indus.crafting.data.DeferredRecipeType;
import com.victorbrndls.indus.crafting.data.DeferredRecipeTypeRegister;
import net.neoforged.bus.api.IEventBus;

public class IndusRecipes {

    private static final DeferredRecipeTypeRegister RECIPE_TYPES = DeferredRecipeTypeRegister.create(Indus.MODID);

    private static final DeferredRecipeSerializerRegister RECIPE_SERIALIZERS = DeferredRecipeSerializerRegister.create(Indus.MODID);

    public static final DeferredRecipeSerializer<MixerRecipe> MIXER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.registerRecipeSerializer(
            "mixer", MixerRecipe.CODEC, MixerRecipe.STREAM_CODEC
    );
    public static final DeferredRecipeSerializer<CrusherRecipe> CRUSHER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.registerRecipeSerializer(
            "crusher", CrusherRecipe.CODEC, CrusherRecipe.STREAM_CODEC
    );
    public static final DeferredRecipeSerializer<AssemblerRecipe> ASSEMBLER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.registerRecipeSerializer(
            "assembler", AssemblerRecipe.CODEC, AssemblerRecipe.STREAM_CODEC
    );
    public static final DeferredRecipeSerializer<MaintenanceDepotRecipe> MAINTENANCE_DEPOT_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.registerRecipeSerializer(
            "maintenance_depot", MaintenanceDepotRecipe.CODEC, MaintenanceDepotRecipe.STREAM_CODEC
    );

    public static final DeferredRecipeType<MixerRecipe> MIXER_RECIPE_TYPE = RECIPE_TYPES.registerRecipeType(
            "mixer"
    );
    public static final DeferredRecipeType<CrusherRecipe> CRUSHER_RECIPE_TYPE = RECIPE_TYPES.registerRecipeType(
            "crusher"
    );
    public static final DeferredRecipeType<AssemblerRecipe> ASSEMBLER_RECIPE_TYPE = RECIPE_TYPES.registerRecipeType(
            "assembler"
    );
    public static final DeferredRecipeType<MaintenanceDepotRecipe> MAINTENANCE_DEPOT_RECIPE_TYPE = RECIPE_TYPES.registerRecipeType(
            "maintenance_depot"
    );

    public static void init(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
    }

}
