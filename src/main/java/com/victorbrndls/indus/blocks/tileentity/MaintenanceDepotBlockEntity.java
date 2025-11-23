package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.crafting.IndusRecipeHelper;
import com.victorbrndls.indus.crafting.IndusRecipes;
import com.victorbrndls.indus.items.MaintenanceTier;
import com.victorbrndls.indus.items.wrapper.VoidingItemHandler;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.mod.structure.IndusStructureStatus;
import com.victorbrndls.indus.world.IndusNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

public class MaintenanceDepotBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos INPUT_1_POS = new BlockPos(7, 1, -4);
    private final static BlockPos INPUT_2_POS = new BlockPos(7, 1, -3);
    private final static BlockPos INPUT_3_POS = new BlockPos(7, 1, -2);

    private final static IItemHandler voidingOutput = new VoidingItemHandler();

    private final static int MAINTENANCE_RATE = 100;

    public MaintenanceDepotBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.MAINTENANCE_DEPOT.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.MAINTENANCE_DEPOT;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        if ((level.getGameTime() % 80) != 0) return;

        if (networkId < 0) {
            setStatus(IndusStructureStatus.NOT_CONNECTED);
            return;
        }
        var networkManager = IndusNetworkManager.get((ServerLevel) level);

        // move this down when we have more types of maintenance
        if (
                networkManager.getAvailableMaintenanceSpace(networkId, MaintenanceTier.BASIC) <= MAINTENANCE_RATE
        ) {
            setStatus(IndusStructureStatus.OUTPUT_FULL);
            return;
        }

        var input1Handler = getRelativeItemHandler(level, INPUT_1_POS);
        var input2Handler = getRelativeItemHandler(level, INPUT_2_POS);
        var input3Handler = getRelativeItemHandler(level, INPUT_3_POS);
        if (input1Handler == null || input2Handler == null || input3Handler == null) {
            setStatus(IndusStructureStatus.INVALID_STRUCTURE);
            return;
        }

        var recipe = IndusRecipeHelper.getRecipe(
                (ServerLevel) level,
                IndusRecipes.MAINTENANCE_DEPOT_RECIPE_TYPE.get(),
                input1Handler,
                input2Handler,
                input3Handler
        );
        if (recipe == null) {
            setStatus(IndusStructureStatus.IDLE);
            return;
        }

        var crafted = IndusRecipeHelper.craftRecipe(
                recipe,
                voidingOutput,
                input1Handler,
                input2Handler,
                input3Handler
        );
        if (!crafted) {
            setStatus(IndusStructureStatus.IDLE);
            return;
        }

        setStatus(IndusStructureStatus.WORKING);

        networkManager.addMaintenance(networkId, MaintenanceTier.BASIC, MAINTENANCE_RATE);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.maintenance_depot");
    }
}