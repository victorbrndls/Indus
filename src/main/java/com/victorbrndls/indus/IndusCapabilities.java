package com.victorbrndls.indus;

import com.victorbrndls.indus.blocks.IndusBlocks;
import com.victorbrndls.indus.blocks.tileentity.Container1BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

@EventBusSubscriber
public final class IndusCapabilities {

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlock(
                Capabilities.Item.BLOCK,
                (level, pos, state, be, side) ->
                        be instanceof Container1BlockEntity crate ? VanillaContainerWrapper.of(crate.getContainer()) : null,
                IndusBlocks.CONTAINER_1.get()
        );
    }
}