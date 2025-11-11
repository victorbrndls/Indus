package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.items.IndusItems;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class SteamGeneratorBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos FUEL_POS = new BlockPos(9, 0, -2);
    private final static BlockPos WATER_POS = new BlockPos(9, 0, -4);

    public SteamGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.STEAM_GENERATOR.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.STEAM_GENERATOR;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        tickCounter++;
        if (tickCounter < 20) return;
        tickCounter = 0;

        ResourceHandler<ItemResource> fuelHandler = getRelativeItemHandler(level, FUEL_POS);
        ResourceHandler<ItemResource> waterHandler = getRelativeItemHandler(level, WATER_POS);

        if (fuelHandler == null || waterHandler == null) return;

        try (var tx = Transaction.open(null)) {
            int fuelUsed = fuelHandler.extract(ItemResource.of(Items.COAL), 1, tx);
            if (fuelUsed < 1) return;

            int waterUsed = waterHandler.extract(ItemResource.of(IndusItems.WATER_CELL.get()), 1, tx);
            if (waterUsed < 1) return;

            tx.commit();
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.steam_generator");
    }
}