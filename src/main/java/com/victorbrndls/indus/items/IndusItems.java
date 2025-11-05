package com.victorbrndls.indus.items;


import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.blocks.IndusBlocks;
import net.minecraft.world.item.BlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IndusItems {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Indus.MODID);

    public static final DeferredItem<BlockItem> TREE_FARM = ITEMS.registerSimpleBlockItem(
            "tree_farm", IndusBlocks.TREE_FARM
    );

    public static void init(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
