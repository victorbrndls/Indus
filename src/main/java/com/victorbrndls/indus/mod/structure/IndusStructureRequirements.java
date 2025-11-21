package com.victorbrndls.indus.mod.structure;

import com.victorbrndls.indus.crafting.AssemblerRecipe;
import com.victorbrndls.indus.crafting.CountedIngredient;
import com.victorbrndls.indus.items.IndusItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.List;

public class IndusStructureRequirements {

    public static List<ItemStack> getRequirements(IndusStructure structure) {
        return switch (structure) {
            // available: concrete slab
            case TREE_FARM -> List.of(
                    new ItemStack(IndusItems.CONCRETE_SLAB.get(), 64),
                    new ItemStack(Items.STONECUTTER, 8),
                    new ItemStack(Items.OAK_SAPLING, 32),
                    new ItemStack(Items.DIAMOND_AXE, 2)
            );
            case QUARRY -> List.of(
                    new ItemStack(IndusItems.CONCRETE_SLAB.get(), 96),
                    new ItemStack(Items.HOPPER, 8),
                    new ItemStack(Items.TORCH, 64),
                    new ItemStack(Items.DIAMOND_PICKAXE, 2),
                    new ItemStack(Items.DIAMOND_SHOVEL, 2)
            );
            case BLAST_FURNACE -> List.of(
                    new ItemStack(IndusItems.CONCRETE_SLAB.get(), 96),
                    new ItemStack(Items.BRICKS, 48),
                    new ItemStack(Items.BLAST_FURNACE, 12),
                    new ItemStack(Items.FURNACE, 16),
                    new ItemStack(Items.HOPPER, 12)
            );
            // available: construction part 1
            case MIXER -> List.of(
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 48),
                    new ItemStack(Items.STONE_BRICKS, 128),
                    new ItemStack(Items.CAULDRON, 32),
                    new ItemStack(Items.HOPPER, 24),
                    new ItemStack(Items.CRAFTING_TABLE, 32)
            );
            case PUMP -> List.of(
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 64),
                    new ItemStack(Items.STONE_BRICKS, 128),
                    new ItemStack(Items.CAULDRON, 32),
                    new ItemStack(Items.HOPPER, 32),
                    new ItemStack(Items.PISTON, 40)
            );
            case STEAM_GENERATOR -> List.of(
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 128),
                    new ItemStack(Items.COPPER_BLOCK, 32),
                    new ItemStack(Items.CAULDRON, 16),
                    new ItemStack(Items.HOPPER, 12),
                    new ItemStack(Items.PISTON, 16)
            );
            case CRUSHER -> List.of(
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 196),
                    new ItemStack(Items.IRON_BLOCK, 32),
                    new ItemStack(Items.GRINDSTONE, 16),
                    new ItemStack(Items.HOPPER, 16),
                    new ItemStack(Items.PISTON, 32)
            );
            case MAINTENANCE_DEPOT -> List.of(
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 128),
                    new ItemStack(Items.IRON_BLOCK, 16),
                    new ItemStack(Items.COPPER_BLOCK, 16),
                    new ItemStack(Items.CRAFTING_TABLE, 16),
                    new ItemStack(Items.PISTON, 32)
            );
            case ASSEMBLER_1 -> List.of(
                    new ItemStack(IndusItems.CONSTRUCTION_PART_1.get(), 256),
                    new ItemStack(Items.IRON_BLOCK, 8),
                    new ItemStack(Items.COPPER_BLOCK, 8),
                    new ItemStack(Items.CRAFTING_TABLE, 32),
                    new ItemStack(Items.CRAFTER, 40)
            );
            // available: construction part 2
        };
    }

    public static List<AssemblerRecipe> getRecipes() {
        return Arrays.stream(IndusStructure.values()).map(s -> {
                    var reqs = getRequirements(s);
                    var output = switch (s) {
                        case TREE_FARM -> IndusItems.TREE_FARM;
                        case QUARRY -> IndusItems.QUARRY;
                        case BLAST_FURNACE -> IndusItems.BLAST_FURNACE;
                        case MIXER -> IndusItems.MIXER;
                        case PUMP -> IndusItems.PUMP;
                        case STEAM_GENERATOR -> IndusItems.STEAM_GENERATOR;
                        case CRUSHER -> IndusItems.CRUSHER;
                        case MAINTENANCE_DEPOT -> IndusItems.MAINTENANCE_DEPOT;
                        case ASSEMBLER_1 -> IndusItems.ASSEMBLER_1;
                    };

                    return new AssemblerRecipe(
                            reqs.stream().map(r -> new CountedIngredient(Ingredient.of(r.getItem()), r.getCount())).toList(),
                            new ItemStack(output.get(), 1)
                    );
                })
                .toList();
    }

}
