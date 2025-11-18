package com.victorbrndls.indus.items;

import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.mod.structure.IndusStructureHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

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
}
