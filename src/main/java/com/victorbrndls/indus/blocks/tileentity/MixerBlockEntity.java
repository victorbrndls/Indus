package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.crafting.IndusRecipes;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.shared.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.List;

public class MixerBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos INPUT_1_POS = new BlockPos(2, 1, -5);
    private final static BlockPos INPUT_2_POS = new BlockPos(3, 2, -5);
    private final static BlockPos INPUT_3_POS = new BlockPos(4, 3, -5);
    private final static BlockPos OUTPUT_POS = new BlockPos(4, 1, 0);

    public MixerBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.MIXER.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.MIXER;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        tickCounter++;

        if (tickCounter >= 20) {
            tickCounter = 0;

            BlockPos input1Pos = BlockHelper.offsetFrontFacing(pos, state, INPUT_1_POS);
            BlockPos input2Pos = BlockHelper.offsetFrontFacing(pos, state, INPUT_2_POS);
            BlockPos input3Pos = BlockHelper.offsetFrontFacing(pos, state, INPUT_3_POS);
            BlockPos outputPos = BlockHelper.offsetFrontFacing(pos, state, OUTPUT_POS);

            ResourceHandler<ItemResource> input1Handler = BlockHelper.getItemHandlerAt(level, input1Pos);
            ResourceHandler<ItemResource> input2Handler = BlockHelper.getItemHandlerAt(level, input2Pos);
            ResourceHandler<ItemResource> input3Handler = BlockHelper.getItemHandlerAt(level, input3Pos);
            ResourceHandler<ItemResource> outputHandler = BlockHelper.getItemHandlerAt(level, outputPos);

            if (input1Handler == null || input2Handler == null || input3Handler == null) return;
            if (outputHandler == null) return;

            var recipes = ((ServerLevel) level).recipeAccess().getRecipes().stream()
                    .filter(r -> r.value().getType() == IndusRecipes.RECIPE_TYPE_MIXER.get())
                    .map(r -> (Recipe<CraftingInput>) r.value())
                    .toList();

            var matchingRecipe = recipes.stream()
                    .filter(r -> r.matches(CraftingInput.of(1, 1, List.of()), level))
                    .findFirst();



            try (var tx = Transaction.open(null)) {

            }
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.mixer");
    }
}