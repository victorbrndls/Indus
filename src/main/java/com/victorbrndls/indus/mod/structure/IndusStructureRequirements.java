package com.victorbrndls.indus.mod.structure;

import com.victorbrndls.indus.items.IndusItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class IndusStructureRequirements {

    public static List<ItemStack> getRequirements(IndusStructure structure) {
        return switch (structure) {
            case TREE_FARM -> List.of(
                    new ItemStack(IndusItems.CONCRETE_SLAB.get(), 32),
                    new ItemStack(Items.OAK_LOG, 48),
                    new ItemStack(Items.CRAFTING_TABLE, 6),
                    new ItemStack(Items.OAK_SAPLING, 8),
                    new ItemStack(Items.LANTERN, 2)
            );
            case QUARRY -> List.of(
                    new ItemStack(IndusItems.CONCRETE_SLAB.get(), 96),
                    new ItemStack(Items.STONE, 128),
                    new ItemStack(Items.GRAVEL, 64),
                    new ItemStack(Items.FURNACE, 8),
                    new ItemStack(Items.IRON_PICKAXE, 1)
            );
        };
    }

}
