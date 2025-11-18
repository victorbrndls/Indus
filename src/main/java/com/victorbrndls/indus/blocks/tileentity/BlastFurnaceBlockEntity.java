package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.items.IndusItems;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.registries.DeferredItem;

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

        var fuelHandler = getRelativeItemHandler(level, FUEL_POS);
        var oreHandler = getRelativeItemHandler(level, ORE_POS);
        var outputHandler = getRelativeItemHandler(level, OUTPUT_POS);

        if (fuelHandler == null || oreHandler == null || outputHandler == null) return;

        int chosenIndex = -1;
        Item outItem = null;

        int size = oreHandler.getSlots();
        for (int i = 0; i < size; i++) {
            ItemStack stack = oreHandler.getStackInSlot(i);
            if (stack.isEmpty() || stack.getCount() < RATE) continue;

            DeferredItem<Item> mapped = RECIPES.get(stack.getItem());
            if (mapped == null) continue;

            chosenIndex = i;
            outItem = mapped.get();
            break;
        }
        if (chosenIndex == -1 || outItem == null) return;

        int coalSlot = -1;
        int fuelSlots = fuelHandler.getSlots();
        for (int i = 0; i < fuelSlots; i++) {
            ItemStack stack = fuelHandler.getStackInSlot(i);
            if (stack.is(Items.COAL) && stack.getCount() >= 1) {
                coalSlot = i;
                break;
            }
        }
        if (coalSlot == -1) return;

        ItemStack toInsert = new ItemStack(outItem, RATE);
        ItemStack remainder = ItemHandlerHelper.insertItem(outputHandler, toInsert, true);
        if (!remainder.isEmpty()) {
            // not enough space for full RATE, abort
            return;
        }

        fuelHandler.extractItem(coalSlot, 1, false);
        oreHandler.extractItem(chosenIndex, RATE, false);
        ItemHandlerHelper.insertItem(outputHandler, new ItemStack(outItem, RATE), false);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.blast_furnace");
    }
}