package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.blocks.IndusBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IndusTileEntities {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Indus.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TreeFarmBlockEntity>> TREE_FARM_BLOCK_ENTITY =
            BLOCK_ENTITY_REGISTER.register("tree_farm", () ->
                    new BlockEntityType<>(TreeFarmBlockEntity::new, IndusBlocks.TREE_FARM.get())
            );

    public static void init(IEventBus eventBus) {
        BLOCK_ENTITY_REGISTER.register(eventBus);
    }

    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {

    }

}
