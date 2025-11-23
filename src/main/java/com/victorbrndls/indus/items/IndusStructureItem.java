package com.victorbrndls.indus.items;

import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.mod.structure.IndusStructureHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class IndusStructureItem extends BlockItem {

    private final IndusStructure structure;

    public IndusStructureItem(
            Block block,
            IndusStructure structure,
            Properties properties
    ) {
        super(block, properties);
        this.structure = structure;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (!level.isClientSide()) return;

        IndusStructureHelper.requestStructure(structure);
    }

    public IndusStructure getStructure() {
        return structure;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        var usesEnergy = false;
        var usesMaintenance = false;

        switch (structure) {
            case TREE_FARM, QUARRY, BLAST_FURNACE, MIXER, STEAM_GENERATOR, MAINTENANCE_DEPOT -> {
            }
            case PUMP -> {
                usesMaintenance = true;
            }
            case CRUSHER, ASSEMBLER_1 -> {
                usesEnergy = true;
                usesMaintenance = true;
            }
        }

        if (usesEnergy) {
            tooltipComponents.add(Component.literal("Consumes ").append(Component.literal("Energy").withColor(0xFF00FF00)));
        }
        if (usesMaintenance) {
            tooltipComponents.add(Component.literal("Consumes ").append(Component.literal("Maintenance").withColor(0xFF00AAFF)));
        }
    }
}
