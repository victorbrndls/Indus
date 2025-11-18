package com.victorbrndls.indus;

import com.victorbrndls.indus.client.GhostStructures;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Indus.MODID, dist = Dist.CLIENT)
public class IndusClient {

    public static GhostStructures GHOST_STRUCTURES = new GhostStructures();

    public IndusClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
