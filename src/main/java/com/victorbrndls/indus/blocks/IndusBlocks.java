package com.victorbrndls.indus.blocks;


import com.victorbrndls.indus.Indus;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IndusBlocks {

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Indus.MODID);

    public static final DeferredHolder<Block, TreeFarmBlock> TREE_FARM = BLOCKS.registerBlock(
            "tree_farm",
            TreeFarmBlock::new
    );

    public static final DeferredHolder<Block, QuarryBlock> QUARRY = BLOCKS.registerBlock(
            "quarry",
            QuarryBlock::new
    );

    public static final DeferredHolder<Block, BlastFurnaceBlock> BLAST_FURNACE = BLOCKS.registerBlock(
            "blast_furnace",
            BlastFurnaceBlock::new
    );

    public static final DeferredHolder<Block, MixerBlock> MIXER = BLOCKS.registerBlock(
            "mixer",
            MixerBlock::new
    );

    public static final DeferredHolder<Block, PumpBlock> PUMP = BLOCKS.registerBlock(
            "pump",
            PumpBlock::new
    );

    public static final DeferredHolder<Block, SteamGeneratorBlock> STEAM_GENERATOR = BLOCKS.registerBlock(
            "steam_generator",
            SteamGeneratorBlock::new
    );

    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
