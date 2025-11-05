package com.victorbrndls.indus.blocks;


import com.victorbrndls.indus.Indus;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IndusBlocks {

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Indus.MODID);

    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("indus_block", () ->
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
    );

    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
