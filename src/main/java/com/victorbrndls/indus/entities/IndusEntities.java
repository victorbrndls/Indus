package com.victorbrndls.indus.entities;

import com.victorbrndls.indus.Indus;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IndusEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, Indus.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<ChunkLoaderMinecart>> CHUNK_LOADER_MINECART = ENTITIES.register("chunk_loader_minecart",
            () -> EntityType.Builder.<ChunkLoaderMinecart>of(ChunkLoaderMinecart::new, MobCategory.MISC)
                    .sized(0.98F, 0.7F)
                    .build("chunk_loader_minecart"));

    public static void init(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

}
