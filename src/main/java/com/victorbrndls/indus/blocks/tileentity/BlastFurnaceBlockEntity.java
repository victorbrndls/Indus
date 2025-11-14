package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.items.IndusItems;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.Map;

public class BlastFurnaceBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos FUEL_POS = new BlockPos(4, 2, -5);
    private final static BlockPos ORE_POS = new BlockPos(1, 1, -5);
    private final static BlockPos OUTPUT_POS = new BlockPos(3, 1, 0);

    private final static int RATE = 4; // items per operation

    private final static Map<Item, DeferredItem<Item>> RECIPES = Map.of(
            Items.RAW_IRON, IndusItems.IRON_PLATE,
            Items.RAW_COPPER, IndusItems.COPPER_PLATE
    );

    public BlastFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.BLAST_FURNACE.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.BLAST_FURNACE;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        if ((level.getGameTime() % 20) != 0) return;

        ResourceHandler<ItemResource> fuelHandler = getRelativeItemHandler(level, FUEL_POS);
        ResourceHandler<ItemResource> oreHandler = getRelativeItemHandler(level, ORE_POS);
        ResourceHandler<ItemResource> outputHandler = getRelativeItemHandler(level, OUTPUT_POS);

        if (fuelHandler == null || oreHandler == null || outputHandler == null) return;

        int chosenIndex = -1;
        ItemResource chosenRes = null;
        DeferredItem<Item> outDeferred = null;

        int size = oreHandler.size();
        for (int i = 0; i < size; i++) {
            long amt = oreHandler.getAmountAsLong(i);
            if (amt < RATE) continue;

            ItemResource res = oreHandler.getResource(i);
            DeferredItem<Item> mapped = RECIPES.get(res.getItem());
            if (mapped == null) continue;

            chosenIndex = i;
            chosenRes = res;
            outDeferred = mapped;
            break;
        }
        if (chosenIndex == -1) return;

        Item outItem = outDeferred.get();

        try (var tx = Transaction.open(null)) {
            int fuelUsed = fuelHandler.extract(ItemResource.of(Items.COAL), 1, tx);
            if (fuelUsed < 1) return;

            int oreUsed = oreHandler.extract(chosenIndex, chosenRes, RATE, tx);
            if (oreUsed < RATE) return;

            int outInserted = outputHandler.insert(ItemResource.of(outItem), RATE, tx);
            if (outInserted == RATE) {
                tx.commit();
            } else {
                tx.close();
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.blast_furnace");
    }
}