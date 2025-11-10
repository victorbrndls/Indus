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

    public static final DeferredItem<Item> PROSPECTOR = ITEMS.registerItem(
            "prospector",
            properties -> new ProspectorItem(properties.stacksTo(1))
    );

    public static final DeferredItem<Item> CONCRETE_SLAB = ITEMS.registerItem(
            "concrete_slab",
            ConcreteSlabItem::new
    );

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

    public static void init(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
