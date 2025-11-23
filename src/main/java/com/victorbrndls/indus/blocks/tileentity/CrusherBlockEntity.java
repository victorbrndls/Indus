package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.crafting.IndusRecipeHelper;
import com.victorbrndls.indus.crafting.IndusRecipes;
import com.victorbrndls.indus.items.MaintenanceTier;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.mod.structure.IndusStructureStatus;
import com.victorbrndls.indus.world.IndusNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CrusherBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos INPUT_POS = new BlockPos(3, 1, -6);
    private final static BlockPos OUTPUT_POS = new BlockPos(3, 1, 0);

    private final static int ENERGY_CONSUMPTION = 10;
    private final static int MAINTENANCE_CONSUMPTION = 1;

    public CrusherBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.CRUSHER.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.CRUSHER;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        if ((level.getGameTime() % 80) != 0) return;

        if (networkId < 0) {
            setStatus(IndusStructureStatus.NOT_CONNECTED);
            return;
        }
        var networkManager = IndusNetworkManager.get((ServerLevel) level);

        var inputHandler = getRelativeItemHandler(level, INPUT_POS);
        var outputHandler = getRelativeItemHandler(level, OUTPUT_POS);

        if (inputHandler == null || outputHandler == null) {
            setStatus(IndusStructureStatus.INVALID_STRUCTURE);
            return;
        }

        var recipe = IndusRecipeHelper.getRecipe(
                (ServerLevel) level,
                IndusRecipes.CRUSHER_RECIPE_TYPE.get(),
                inputHandler
        );
        if (recipe == null) {
            setStatus(IndusStructureStatus.IDLE);
            return;
        }

        var fits = IndusRecipeHelper.fits(outputHandler, recipe);
        if (!fits) {
            setStatus(IndusStructureStatus.OUTPUT_FULL);
            return;
        }

        var canRun = consumeMaintenanceAndCheck(MaintenanceTier.BASIC, MAINTENANCE_CONSUMPTION);
        if (!canRun) {
            setStatus(IndusStructureStatus.NO_MAINTENANCE);
            return;
        }

        var consumedEnergy = networkManager.consumeEnergy(networkId, ENERGY_CONSUMPTION);
        if (!consumedEnergy) {
            setStatus(IndusStructureStatus.NO_ENERGY);
            return;
        }

        setStatus(IndusStructureStatus.WORKING);

        IndusRecipeHelper.craftRecipe(
                recipe,
                outputHandler,
                inputHandler
        );
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.crusher");
    }
}