package com.victorbrndls.indus;

import com.victorbrndls.indus.client.GhostStructures;
import com.victorbrndls.indus.client.IndusClientEvents;
import com.victorbrndls.indus.entities.IndusEntities;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = Indus.MODID, dist = Dist.CLIENT)
public class IndusClient {

    public static GhostStructures GHOST_STRUCTURES = new GhostStructures();

    public IndusClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        container.getEventBus().addListener(IndusClient::registerEntityRenderers);

        NeoForge.EVENT_BUS.addListener(IndusClientEvents::onTick);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(IndusEntities.CHUNK_LOADER_MINECART.get(),
                context -> new MinecartRenderer(context, ModelLayers.HOPPER_MINECART));
    }
}
