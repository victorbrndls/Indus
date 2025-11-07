package com.victorbrndls.indus.blocks.structure;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class IndusStructureRequirements {

    public static List<ItemStack> getRequirements(IndusStructure structure) {
        return switch (structure) {
            case TREE_FARM -> List.of(
                    new ItemStack(Items.OAK_LOG, 32),
                    new ItemStack(Items.CRAFTING_TABLE, 4),
                    new ItemStack(Items.STONECUTTER, 1),
                    new ItemStack(Items.OAK_SAPLING, 8),
                    new ItemStack(Items.LANTERN, 2)
            );
            case QUARRY -> List.of();
        };
    }

}
