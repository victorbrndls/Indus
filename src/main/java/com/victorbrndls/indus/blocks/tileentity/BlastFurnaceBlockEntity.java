package com.victorbrndls.indus.blocks.tileentity;

import com.victorbrndls.indus.gui.BaseStructureMenu;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.shared.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

public class BlastFurnaceBlockEntity extends BaseStructureBlockEntity {

    private final static BlockPos FUEL_POS = new BlockPos(4, 2, -5);
    private final static BlockPos ORE_POS = new BlockPos(1, 1, -5);
    private final static BlockPos OUTPUT_POS = new BlockPos(3, 1, 0);

    public BlastFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(IndusTileEntities.BLAST_FURNACE_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public IndusStructure getStructureType() {
        return IndusStructure.BLAST_FURNACE;
    }

    @Override
    protected void tickBuilt(Level level, BlockPos pos, BlockState state) {
        tickCounter++;

        if (tickCounter >= 20) {
            tickCounter = 0;

            BlockPos fuelInput = BlockHelper.offsetFrontFacing(pos, state, FUEL_POS);
            BlockPos oreInput = BlockHelper.offsetFrontFacing(pos, state, ORE_POS);
            BlockPos outputPos = BlockHelper.offsetFrontFacing(pos, state, OUTPUT_POS);

            ResourceHandler<ItemResource> fuelHandler = BlockHelper.getItemHandlerAt(level, fuelInput);
            ResourceHandler<ItemResource> oreHandler = BlockHelper.getItemHandlerAt(level, oreInput);
            ResourceHandler<ItemResource> outputHandler = BlockHelper.getItemHandlerAt(level, outputPos);

            if (fuelHandler == null) return;
            if (oreHandler == null) return;
            if (outputHandler == null) return;

            try (var tx = Transaction.open(null)) {
                var fuelResource = fuelHandler.extract(ItemResource.of(Items.COAL), 1, tx);
                if (fuelResource > 0) {
                    var oreResource = oreHandler.extract(ItemResource.of(Items.RAW_IRON), 1, tx);
                    if (oreResource > 0) {
                        // Successfully extracted fuel and ore, now insert the output
                        var inserted = outputHandler.insert(ItemResource.of(Items.IRON_INGOT), 1, tx);
                        if (inserted == 1) {
                            tx.commit();
                        }
                    }
                }
            }
        }
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BaseStructureMenu(id, inventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.indus.blast_furnace");
    }
}