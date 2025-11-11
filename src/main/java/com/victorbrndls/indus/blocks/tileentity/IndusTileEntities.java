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

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TreeFarmBlockEntity>> TREE_FARM =
            BLOCK_ENTITY_REGISTER.register("tree_farm", () ->
                    new BlockEntityType<>(TreeFarmBlockEntity::new, IndusBlocks.TREE_FARM.get())
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<QuarryBlockEntity>> QUARRY =
            BLOCK_ENTITY_REGISTER.register("quarry", () ->
                    new BlockEntityType<>(QuarryBlockEntity::new, IndusBlocks.QUARRY.get())
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlastFurnaceBlockEntity>> BLAST_FURNACE =
            BLOCK_ENTITY_REGISTER.register("blast_furnace", () ->
                    new BlockEntityType<>(BlastFurnaceBlockEntity::new, IndusBlocks.BLAST_FURNACE.get())
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MixerBlockEntity>> MIXER =
            BLOCK_ENTITY_REGISTER.register("mixer", () ->
                    new BlockEntityType<>(MixerBlockEntity::new, IndusBlocks.MIXER.get())
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PumpBlockEntity>> PUMP =
            BLOCK_ENTITY_REGISTER.register("pump", () ->
                    new BlockEntityType<>(PumpBlockEntity::new, IndusBlocks.PUMP.get())
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SteamGeneratorBlockEntity>> STEAM_GENERATOR =
            BLOCK_ENTITY_REGISTER.register("steam_generator", () ->
                    new BlockEntityType<>(SteamGeneratorBlockEntity::new, IndusBlocks.STEAM_GENERATOR.get())
            );

    public static void init(IEventBus eventBus) {
        BLOCK_ENTITY_REGISTER.register(eventBus);
    }

    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {

    }

}
