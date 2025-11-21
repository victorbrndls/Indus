package com.victorbrndls.indus.datagen;

import com.victorbrndls.indus.blocks.IndusBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;
import java.util.Set;

public class IndusBlockLoot extends BlockLootSubProvider {

    public IndusBlockLoot(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        this.add(IndusBlocks.QUARRY.get(), IndusBlockLoot::createStandardTable);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return List.of(IndusBlocks.QUARRY.get());
    }

    private static LootTable.Builder createStandardTable(Block block) {
        var builder = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block))
                .when(ExplosionCondition.survivesExplosion());
        return LootTable.lootTable().withPool(builder);
    }
}
