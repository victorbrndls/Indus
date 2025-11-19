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
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class IndusTileEntities {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Indus.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TreeFarmBlockEntity>> TREE_FARM =
            BLOCK_ENTITY_REGISTER.register("tree_farm", () ->
                    BlockEntityType.Builder.of(TreeFarmBlockEntity::new, IndusBlocks.TREE_FARM.get()).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<QuarryBlockEntity>> QUARRY =
            BLOCK_ENTITY_REGISTER.register("quarry", () ->
                    BlockEntityType.Builder.of(QuarryBlockEntity::new, IndusBlocks.QUARRY.get()).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlastFurnaceBlockEntity>> BLAST_FURNACE =
            BLOCK_ENTITY_REGISTER.register("blast_furnace", () ->
                    BlockEntityType.Builder.of(BlastFurnaceBlockEntity::new, IndusBlocks.BLAST_FURNACE.get()).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MixerBlockEntity>> MIXER =
            BLOCK_ENTITY_REGISTER.register("mixer", () ->
                    BlockEntityType.Builder.of(MixerBlockEntity::new, IndusBlocks.MIXER.get()).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PumpBlockEntity>> PUMP =
            BLOCK_ENTITY_REGISTER.register("pump", () ->
                    BlockEntityType.Builder.of(PumpBlockEntity::new, IndusBlocks.PUMP.get()).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SteamGeneratorBlockEntity>> STEAM_GENERATOR = register(
            "steam_generator", SteamGeneratorBlockEntity::new, IndusBlocks.STEAM_GENERATOR::get
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrusherBlockEntity>> CRUSHER = register(
            "crusher", CrusherBlockEntity::new, IndusBlocks.CRUSHER::get
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MaintenanceDepotBlockEntity>> MAINTENANCE_DEPOT = register(
            "maintenance_depot", MaintenanceDepotBlockEntity::new, IndusBlocks.MAINTENANCE_DEPOT::get
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<Assembler1BlockEntity>> ASSEMBLER_1 = register(
            "assembler_1", Assembler1BlockEntity::new, IndusBlocks.ASSEMBLER_1::get
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<Container1BlockEntity>> CONTAINER_1 = register(
            "container_1", Container1BlockEntity::new, IndusBlocks.CONTAINER_1::get
    );

    // Cart
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<UnloaderBlockEntity>> CART_UNLOADER = register(
            "cart_unloader", UnloaderBlockEntity::new, IndusBlocks.CART_UNLOADER::get
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LoaderBlockEntity>> CART_LOADER = register(
            "cart_loader", LoaderBlockEntity::new, IndusBlocks.CART_LOADER::get
    );

    public static void init(IEventBus eventBus) {
        BLOCK_ENTITY_REGISTER.register(eventBus);
    }

    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                IndusTileEntities.CONTAINER_1.get(),
                (entity, direction) -> new InvWrapper(entity.getContainer())
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                IndusTileEntities.CART_UNLOADER.get(),
                (entity, direction) -> new InvWrapper(entity.getContainer())
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                IndusTileEntities.CART_LOADER.get(),
                (entity, direction) -> new InvWrapper(entity.getContainer())
        );
    }

    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(
            String name,
            BlockEntityType.BlockEntitySupplier<T> supplier,
            Supplier<Block> validBlock
    ) {
        return BLOCK_ENTITY_REGISTER.register(name, () -> BlockEntityType.Builder.of(supplier, validBlock.get()).build(null));
    }

}
