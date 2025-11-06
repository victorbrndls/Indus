package com.victorbrndls.indus.items;


import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.blocks.IndusBlocks;
import com.victorbrndls.indus.blocks.structure.IndusStructure;
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

    public static final DeferredItem<Item> PROSPECTOR = ITEMS.registerItem(
            "prospector",
            properties -> new ProspectorItem(properties.stacksTo(1))
    );

    public static void init(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
