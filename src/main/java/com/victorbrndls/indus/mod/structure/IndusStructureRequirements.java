package com.victorbrndls.indus.mod.structure;

import com.victorbrndls.indus.items.IndusItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class IndusStructureRequirements {

    public static List<ItemStack> getRequirements(IndusStructure structure) {
        return switch (structure) {
            // available: concrete slab
            case TREE_FARM -> List.of(
                    new ItemStack(IndusItems.CONCRETE_SLAB.get(), 64),
                    new ItemStack(Items.STONECUTTER, 8),
                    new ItemStack(Items.OAK_SAPLING, 32),
                    new ItemStack(Items.DIAMOND_AXE, 4)
            );
            case QUARRY -> List.of(
                    new ItemStack(IndusItems.CONCRETE_SLAB.get(), 96),
                    new ItemStack(Items.HOPPER, 8),
                    new ItemStack(Items.TORCH, 64),
                    new ItemStack(Items.DIAMOND_PICKAXE, 4),
                    new ItemStack(Items.DIAMOND_SHOVEL, 4)
            );
            case BLAST_FURNACE -> List.of(
                    new ItemStack(IndusItems.CONCRETE_SLAB.get(), 96),
                    new ItemStack(Items.BRICKS, 48),
                    new ItemStack(Items.BLAST_FURNACE, 16),
                    new ItemStack(Items.FURNACE, 16),
                    new ItemStack(Items.HOPPER, 12)
            );
            // available: construction part 1
            case MIXER -> List.of(
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 32),
                    new ItemStack(Items.STONE_BRICKS, 48),
                    new ItemStack(Items.CAULDRON, 16),
                    new ItemStack(Items.HOPPER, 12),
                    new ItemStack(Items.CRAFTING_TABLE, 4)
            );
            case PUMP -> List.of(
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 40),
                    new ItemStack(Items.STONE_BRICKS, 48),
                    new ItemStack(Items.CAULDRON, 16),
                    new ItemStack(Items.HOPPER, 12),
                    new ItemStack(Items.PISTON, 16)
            );
            case STEAM_GENERATOR -> List.of(
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 64),
                    new ItemStack(Items.COPPER_BLOCK, 32),
                    new ItemStack(Items.CAULDRON, 16),
                    new ItemStack(Items.HOPPER, 12),
                    new ItemStack(Items.PISTON, 16)
            );
            case CRUSHER -> List.of(
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 64),
                    new ItemStack(Items.IRON_BLOCK, 32),
                    new ItemStack(Items.GRINDSTONE, 16),
                    new ItemStack(Items.HOPPER, 16),
                    new ItemStack(Items.PISTON, 32)
            );
            case MAINTENANCE_DEPOT -> List.of(
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 96),
                    new ItemStack(Items.IRON_BLOCK, 16),
                    new ItemStack(Items.COPPER_BLOCK, 16),
                    new ItemStack(Items.CRAFTING_TABLE, 16),
                    new ItemStack(Items.PISTON, 32)
            );
            case ASSEMBLER_1 -> List.of(
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 96),
                    new ItemStack(Items.IRON_BLOCK, 8),
                    new ItemStack(Items.COPPER_BLOCK, 8),
                    new ItemStack(Items.CRAFTING_TABLE, 32),
                    new ItemStack(Items.CRAFTER, 40)
            );
        };
    }

}
