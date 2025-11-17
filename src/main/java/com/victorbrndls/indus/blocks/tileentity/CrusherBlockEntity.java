package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.crafting.IndusRecipeHelper;
import com.victorbrndls.indus.crafting.IndusRecipes;
import com.victorbrndls.indus.items.MaintenanceTier;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.world.IndusNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class CrusherBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos INPUT_POS = new BlockPos(3, 1, -6);
    private final static BlockPos OUTPUT_POS = new BlockPos(3, 1, 0);

    private final static int MAINTENANCE_RATE = 1;

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

        ResourceHandler<ItemResource> inputHandler = getRelativeItemHandler(level, INPUT_POS);
        ResourceHandler<ItemResource> outputHandler = getRelativeItemHandler(level, OUTPUT_POS);

        if (inputHandler == null || outputHandler == null) return;

        var recipe = IndusRecipeHelper.getRecipe(
                (ServerLevel) level,
                IndusRecipes.CRUSHER_RECIPE_TYPE.get(),
                inputHandler
        );
        if (recipe == null) return;

        var canRun = consumeMaintenanceAndCheck(MaintenanceTier.BASIC, MAINTENANCE_RATE);
        if (!canRun) return;

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