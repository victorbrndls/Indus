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
                    new ItemStack(Items.STONECUTTER, 2),
                    new ItemStack(Items.IRON_AXE, 1)
            );
            case QUARRY -> List.of(
                    new ItemStack(IndusItems.CONCRETE_SLAB.get(), 96),
                    new ItemStack(Items.STONE, 128),
                    new ItemStack(Items.GRAVEL, 64),
                    new ItemStack(Items.FURNACE, 8),
                    new ItemStack(Items.IRON_PICKAXE, 1)
            );
            case BLAST_FURNACE -> List.of(
                    new ItemStack(IndusItems.CONCRETE_SLAB.get(), 96),
                    new ItemStack(Items.BRICKS, 48),
                    new ItemStack(Items.BLAST_FURNACE, 8),
                    new ItemStack(Items.HOPPER, 8),
                    new ItemStack(Items.COAL_BLOCK, 8)
            );
            case MIXER -> List.of(
                    new ItemStack(IndusItems.CONCRETE_SLAB.get(), 96),
                    new ItemStack(Items.STONE_BRICKS, 48),
                    new ItemStack(Items.CAULDRON, 8),
                    new ItemStack(Items.HOPPER, 4),
                    new ItemStack(Items.OBSIDIAN, 4)
            );
            case PUMP -> List.of(
                    new ItemStack(IndusItems.CONCRETE_SLAB.get(), 96),
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 8),
                    new ItemStack(Items.CAULDRON, 8),
                    new ItemStack(Items.HOPPER, 4),
                    new ItemStack(Items.PISTON, 4)
            );
            case STEAM_GENERATOR -> List.of(
                    new ItemStack(IndusItems.CONCRETE_SLAB.get(), 96),
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 8),
                    new ItemStack(Items.COPPER_BLOCK, 16),
                    new ItemStack(Items.HOPPER, 4),
                    new ItemStack(Items.PISTON, 4)
            );
            case CRUSHER -> List.of(
                    new ItemStack(IndusItems.CONCRETE_SLAB.get(), 96),
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 8),
                    new ItemStack(Items.COPPER_BLOCK, 16),
                    new ItemStack(Items.HOPPER, 4),
                    new ItemStack(Items.PISTON, 8)
            );
            case MAINTENANCE_DEPOT -> List.of(
                    new ItemStack(IndusItems.CONCRETE_SLAB.get(), 96),
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 8),
                    new ItemStack(Items.COPPER_BLOCK, 16),
                    new ItemStack(Items.HOPPER, 4),
                    new ItemStack(Items.PISTON, 2)
            );
            case ASSEMBLER_1 -> List.of(
                    new ItemStack(IndusItems.CONCRETE_SLAB.get(), 96),
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 8),
                    new ItemStack(Items.COPPER_BLOCK, 16),
                    new ItemStack(Items.HOPPER, 4),
                    new ItemStack(Items.PISTON, 1)
            );
        };
    }

}
