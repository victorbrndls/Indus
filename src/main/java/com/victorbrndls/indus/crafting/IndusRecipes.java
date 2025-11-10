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

    public static final DeferredRecipeType<MixerRecipe> RECIPE_TYPE_MIXER = RECIPE_TYPES.registerRecipeType("mixer");

    public static void init(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
    }

}
