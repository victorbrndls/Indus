package com.victorbrndls.indus;

import com.victorbrndls.indus.blocks.IndusBlocks;
import com.victorbrndls.indus.blocks.tileentity.IndusTileEntities;
import com.victorbrndls.indus.items.IndusItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Indus.MODID)
@EventBusSubscriber(modid = Indus.MODID)
public class Indus {
    public static final String MODID = "indus";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Indus(IEventBus eventBus, ModContainer modContainer) {
        eventBus.addListener(IndusTileEntities::onRegisterCapabilities);

        IndusBlocks.init(eventBus);
        IndusItems.init(eventBus);
        IndusTileEntities.init(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    static void onRegisterPayloadHandler(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID).versioned("0");

    }

}
