package com.victorbrndls.indus.items;


import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.blocks.IndusBlocks;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IndusItems {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Indus.MODID);

    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "indus_item", IndusBlocks.EXAMPLE_BLOCK
    );

    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("indus_food",
            ((properties) -> new Item.Properties().food(
                    new FoodProperties.Builder()
                            .alwaysEdible()
                            .nutrition(1)
                            .saturationModifier(2f)
                            .build()
            ))
    );

    public static void init(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
