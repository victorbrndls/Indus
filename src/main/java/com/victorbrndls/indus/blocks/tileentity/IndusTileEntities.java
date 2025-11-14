package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.blocks.IndusBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

import java.util.function.Supplier;

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

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SteamGeneratorBlockEntity>> STEAM_GENERATOR = register(
            "steam_generator", SteamGeneratorBlockEntity::new, IndusBlocks.STEAM_GENERATOR::get
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrusherBlockEntity>> CRUSHER = register(
            "crusher", CrusherBlockEntity::new, IndusBlocks.CRUSHER::get
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<Container1BlockEntity>> CONTAINER_1 = register(
            "container_1", Container1BlockEntity::new, IndusBlocks.CONTAINER_1::get
    );

    public static void init(IEventBus eventBus) {
        BLOCK_ENTITY_REGISTER.register(eventBus);
    }

    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlock(
                Capabilities.Item.BLOCK,
                (level, pos, state, be, side) ->
                        be instanceof Container1BlockEntity crate ? VanillaContainerWrapper.of(crate.getContainer()) : null,
                IndusBlocks.CONTAINER_1.get()
        );
    }

    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(
            String name,
            BlockEntityType.BlockEntitySupplier<T> supplier,
            Supplier<Block> validBlock
    ) {
        return BLOCK_ENTITY_REGISTER.register(name, () -> new BlockEntityType<>(supplier, validBlock.get()));
    }

}
