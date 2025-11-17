package com.victorbrndls.indus.blocks.tileentity;

import com.mojang.serialization.Codec;
import com.victorbrndls.indus.crafting.IndusRecipeHelper;
import com.victorbrndls.indus.crafting.IndusRecipes;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.world.IndusNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.VoidingResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class MaintenanceDepotBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos INPUT_1_POS = new BlockPos(7, 1, -4);
    private final static BlockPos INPUT_2_POS = new BlockPos(7, 1, -3);
    private final static BlockPos INPUT_3_POS = new BlockPos(7, 1, -2);

    private final static ResourceHandler<ItemResource> voidingOutput = new VoidingResourceHandler<>(ItemResource.EMPTY);

    private final static int MAINTENANCE_RATE = 5;

    private int remainingMaintenance = 0;

    public MaintenanceDepotBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.MAINTENANCE_DEPOT.get(), pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        onConnectedToNetwork();
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.MAINTENANCE_DEPOT;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        if (networkId < 0) return;
        var energyManager = IndusNetworkManager.get((ServerLevel) level);

        if (remainingMaintenance > 0) {
            remainingMaintenance -= energyManager.addEnergy(networkId, remainingMaintenance);
            if (remainingMaintenance > 0) return;
        }

        if ((level.getGameTime() % 80) != 0) return;

        var input1Handler = getRelativeItemHandler(level, INPUT_1_POS);
        var input2Handler = getRelativeItemHandler(level, INPUT_2_POS);
        var input3Handler = getRelativeItemHandler(level, INPUT_3_POS);
        if (input1Handler == null || input2Handler == null || input3Handler == null) return;

        var recipe = IndusRecipeHelper.getRecipe(
                (ServerLevel) level,
                IndusRecipes.MAINTENANCE_DEPOT_RECIPE_TYPE.get(),
                input1Handler,
                input2Handler,
                input3Handler
        );
        if (recipe == null) return;

        var crafted = IndusRecipeHelper.craftRecipe(
                recipe,
                voidingOutput,
                input1Handler,
                input2Handler,
                input3Handler
        );

        if (crafted) {
            int addedEnergy = energyManager.addEnergy(networkId, MAINTENANCE_RATE);
            if (addedEnergy <= 0) return;

            remainingMaintenance = MAINTENANCE_RATE - addedEnergy;
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
            IndusNetworkManager.get((ServerLevel) level).removeEnergyCapacity(networkId, MAINTENANCE_RATE);
        }
    }

    @Override
    protected void onConnectedToNetwork() {
        if (canInteractWithNetwork()) {
            IndusNetworkManager.get((ServerLevel) level).addEnergyCapacity(networkId, MAINTENANCE_RATE);
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
        output.store("remainingEnergy", Codec.INT, remainingMaintenance);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.getInt("remainingEnergy").ifPresent(i -> remainingMaintenance = i);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.maintenance_depot");
    }
}