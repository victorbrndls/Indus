package com.victorbrndls.indus.items;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.IndusClient;
import com.victorbrndls.indus.blocks.structure.IndusStructure;
import com.victorbrndls.indus.blocks.structure.IndusStructureHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

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
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);
        if (!level.isClientSide()) return;

        var structureCache = Indus.STRUCTURE_CACHE;

        if (structureCache.shouldRequest(structure)) {
            structureCache.onRequestMade(structure);

            Indus.LOGGER.info("Requesting structure: {}", structure.name());
            IndusStructureHelper.requestStructure(structure);
        }
    }


    public IndusStructure getStructure() {
        return structure;
    }
}
