package com.victorbrndls.indus.blocks.tileentity;

import com.mojang.serialization.Codec;
import com.victorbrndls.indus.items.IndusItems;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.world.IndusEnergyManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class SteamGeneratorBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos FUEL_POS = new BlockPos(9, 0, -2);
    private final static BlockPos WATER_POS = new BlockPos(9, 0, -4);

    private final static int ENERGY_RATE = 20;

    private int remainingEnergy = 0;

    public SteamGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.STEAM_GENERATOR.get(), pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        onConnectedToNetwork();
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.STEAM_GENERATOR;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        if (networkId < 0) return;
        var energyManager = IndusEnergyManager.get((ServerLevel) level);

        if (remainingEnergy > 0) {
            remainingEnergy -= energyManager.addEnergy(networkId, remainingEnergy);
            if (remainingEnergy > 0) return;
        }

        if ((level.getGameTime() % 20) != 0) return;

        ResourceHandler<ItemResource> fuelHandler = getRelativeItemHandler(level, FUEL_POS);
        ResourceHandler<ItemResource> waterHandler = getRelativeItemHandler(level, WATER_POS);
        if (fuelHandler == null || waterHandler == null) return;

        try (var tx = Transaction.open(null)) {
            int fuelUsed = fuelHandler.extract(ItemResource.of(Items.COAL), 1, tx);
            if (fuelUsed < 1) return;

            int waterUsed = waterHandler.extract(ItemResource.of(IndusItems.WATER_CELL.get()), 1, tx);
            if (waterUsed < 1) return;

            int addedEnergy = energyManager.addEnergy(networkId, ENERGY_RATE);
            if (addedEnergy <= 0) return;

            remainingEnergy = ENERGY_RATE - addedEnergy;
            tx.commit();
        }
    }

    @Override
    protected void onAfterBuilt(Level level, BlockPos pos, BlockState state) {
        super.onAfterBuilt(level, pos, state);
        onConnectedToNetwork();
    }

    @Override
    protected void onDisconnectFromNetwork() {
        if (canInteractWithNetwork()) {
            IndusEnergyManager.get((ServerLevel) level).removeCapacity(networkId, ENERGY_RATE);
        }
    }

    @Override
    protected void onConnectedToNetwork() {
        if (canInteractWithNetwork()) {
            IndusEnergyManager.get((ServerLevel) level).addCapacity(networkId, ENERGY_RATE);
        }
    }

    @Override
    public void setRemoved() {
        onDisconnectFromNetwork();
        super.setRemoved();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("remainingEnergy", Codec.INT, remainingEnergy);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.getInt("remainingEnergy").ifPresent(i -> remainingEnergy = i);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.steam_generator");
    }
}