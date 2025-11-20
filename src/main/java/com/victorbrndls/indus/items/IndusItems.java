package com.victorbrndls.indus.items;


import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.blocks.IndusBlocks;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IndusItems {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Indus.MODID);

    // Structures
    public static final DeferredItem<BlockItem> TREE_FARM = ITEMS.registerItem(
            "tree_farm", properties -> new IndusStructureItem(
                    IndusBlocks.TREE_FARM.get(),
                    IndusStructure.TREE_FARM,
                    properties
            )
    );

    public static final DeferredItem<BlockItem> QUARRY = ITEMS.registerItem(
            "quarry", properties -> new IndusStructureItem(
                    IndusBlocks.QUARRY.get(),
                    IndusStructure.QUARRY,
                    properties
            )
    );

    public static final DeferredItem<BlockItem> BLAST_FURNACE = ITEMS.registerItem(
            "blast_furnace", properties -> new IndusStructureItem(
                    IndusBlocks.BLAST_FURNACE.get(),
                    IndusStructure.BLAST_FURNACE,
                    properties
            )
    );

    public static final DeferredItem<BlockItem> MIXER = ITEMS.registerItem(
            "mixer", properties -> new IndusStructureItem(
                    IndusBlocks.MIXER.get(),
                    IndusStructure.MIXER,
                    properties
            )
    );

    public static final DeferredItem<BlockItem> PUMP = ITEMS.registerItem(
            "pump", properties -> new IndusStructureItem(
                    IndusBlocks.PUMP.get(),
                    IndusStructure.PUMP,
                    properties
            )
    );

    public static final DeferredItem<BlockItem> STEAM_GENERATOR = ITEMS.registerItem(
            "steam_generator", properties -> new IndusStructureItem(
                    IndusBlocks.STEAM_GENERATOR.get(),
                    IndusStructure.STEAM_GENERATOR,
                    properties
            )
    );

    public static final DeferredItem<BlockItem> CRUSHER = ITEMS.registerItem(
            "crusher", properties -> new IndusStructureItem(
                    IndusBlocks.CRUSHER.get(),
                    IndusStructure.CRUSHER,
                    properties
            )
    );

    public static final DeferredItem<BlockItem> MAINTENANCE_DEPOT = ITEMS.registerItem(
            "maintenance_depot", properties -> new IndusStructureItem(
                    IndusBlocks.MAINTENANCE_DEPOT.get(),
                    IndusStructure.MAINTENANCE_DEPOT,
                    properties
            )
    );

    public static final DeferredItem<BlockItem> ASSEMBLER_1 = ITEMS.registerItem(
            "assembler_1", properties -> new IndusStructureItem(
                    IndusBlocks.ASSEMBLER_1.get(),
                    IndusStructure.ASSEMBLER_1,
                    properties
            )
    );

    public static final DeferredItem<BlockItem> CONTAINER_1 = ITEMS.registerItem(
            "container_1", properties -> new BlockItem(
                    IndusBlocks.CONTAINER_1.get(), properties
            )
    );

    public static final DeferredItem<Item> PROSPECTOR = ITEMS.registerItem(
            "prospector",
            properties -> new ProspectorItem(properties.stacksTo(1))
    );
    public static final DeferredItem<Item> WRENCH = ITEMS.registerItem(
            "wrench",
            properties -> new WrenchItem(properties.stacksTo(1))
    );

    // Construction Parts
    public static final DeferredItem<Item> CONSTRUCTION_PART_1 = ITEMS.registerItem(
            "construction_part_1",
            properties -> new ConstructionPartItem(ConstructionPartTier.BASIC, properties)
    );
    public static final DeferredItem<Item> CONSTRUCTION_PART_2 = ITEMS.registerItem(
            "construction_part_2",
            properties -> new ConstructionPartItem(ConstructionPartTier.ADVANCED, properties)
    );
    public static final DeferredItem<Item> CONSTRUCTION_PART_3 = ITEMS.registerItem(
            "construction_part_3",
            properties -> new ConstructionPartItem(ConstructionPartTier.ELITE, properties)
    );
    public static final DeferredItem<Item> CONSTRUCTION_PART_4 = ITEMS.registerItem(
            "construction_part_4",
            properties -> new ConstructionPartItem(ConstructionPartTier.ULTIMATE, properties)
    );

    // Maintenance
    public static final DeferredItem<Item> MAINTENANCE_1 = ITEMS.registerItem(
            "maintenance_1",
            properties -> new MaintenanceItem(MaintenanceTier.BASIC, properties)
    );
    public static final DeferredItem<Item> MAINTENANCE_2 = ITEMS.registerItem(
            "maintenance_2",
            properties -> new MaintenanceItem(MaintenanceTier.ADVANCED, properties)
    );
    public static final DeferredItem<Item> MAINTENANCE_3 = ITEMS.registerItem(
            "maintenance_3",
            properties -> new MaintenanceItem(MaintenanceTier.ELITE, properties)
    );

    // Plates
    public static final DeferredItem<Item> IRON_PLATE = ITEMS.registerItem(
            "iron_plate",
            IronPlateItem::new
    );
    public static final DeferredItem<Item> COPPER_PLATE = ITEMS.registerItem(
            "copper_plate",
            CopperPlateItem::new
    );
    public static final DeferredItem<Item> STEEL_PLATE = ITEMS.registerItem(
            "steel_plate",
            Item::new
    );

    // Materials
    public static final DeferredItem<Item> CONCRETE_SLAB = ITEMS.registerItem(
            "concrete_slab",
            ConcreteSlabItem::new
    );
    public static final DeferredItem<Item> CARBON_FIBER = ITEMS.registerItem(
            "carbon_fiber",
            Item::new
    );
    public static final DeferredItem<Item> WOOD_PULP = ITEMS.registerItem(
            "wood_pulp",
            Item::new
    );
    public static final DeferredItem<Item> COAL_DUST = ITEMS.registerItem(
            "coal_dust",
            Item::new
    );

    // Cells
    public static final DeferredItem<Item> WATER_CELL = ITEMS.registerItem(
            "water_cell",
            WaterCellItem::new
    );
    public static final DeferredItem<Item> CRUDE_OIL_CELL = ITEMS.registerItem(
            "crude_oil_cell",
            CrudeOilCellItem::new
    );

    // Carts
    public static final DeferredItem<Item> CHUNK_LOADER_MINECART = ITEMS.registerItem(
            "chunk_loader_minecart",
            properties -> new ChunkLoaderMinecartItem(properties.stacksTo(1))
    );
    public static final DeferredItem<BlockItem> CART_UNLOADER = ITEMS.registerItem(
            "cart_unloader", properties -> new BlockItem(
                    IndusBlocks.CART_UNLOADER.get(), properties
            )
    );
    public static final DeferredItem<BlockItem> CART_LOADER = ITEMS.registerItem(
            "cart_loader", properties -> new BlockItem(
                    IndusBlocks.CART_LOADER.get(), properties
            )
    );

    public static void init(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
