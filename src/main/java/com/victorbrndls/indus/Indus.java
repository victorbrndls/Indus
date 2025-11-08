package com.victorbrndls.indus;

import com.victorbrndls.indus.blocks.IndusBlocks;
import com.victorbrndls.indus.blocks.tileentity.IndusTileEntities;
import com.victorbrndls.indus.client.IndusStructureCache;
import com.victorbrndls.indus.client.screen.BaseStructureScreen;
import com.victorbrndls.indus.gui.IndusMenus;
import com.victorbrndls.indus.items.IndusItems;
import com.victorbrndls.indus.network.ReceiveStructureMessage;
import com.victorbrndls.indus.network.RequestStructureMessage;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Indus.MODID)
public class Indus {
    public static final String MODID = "indus";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static IndusStructureCache STRUCTURE_CACHE = new IndusStructureCache();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> INDUS_TAB =
            CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .title(Component.literal("Indus"))
                    .icon(() -> new ItemStack(IndusItems.TREE_FARM.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(new ItemStack(IndusItems.TREE_FARM.get()));
                        output.accept(new ItemStack(IndusItems.QUARRY.get()));
                        output.accept(new ItemStack(IndusItems.PROSPECTOR.get()));
                        output.accept(new ItemStack(IndusItems.CONCRETE_SLAB.get()));
                    }).build());

    public Indus(IEventBus eventBus, ModContainer modContainer) {
        eventBus.addListener(IndusTileEntities::onRegisterCapabilities);

        IndusBlocks.init(eventBus);
        IndusItems.init(eventBus);
        IndusTileEntities.init(eventBus);
        IndusMenus.init(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);

        eventBus.addListener(this::handleRegisterMenuScreens);
        eventBus.addListener(this::registerPayloads);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(Indus.MODID, path);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(MODID).versioned("1");

        RequestStructureMessage.register(registrar);
        ReceiveStructureMessage.register(registrar);
    }

    private void handleRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(IndusMenus.BASE_STRUCTURE.get(), BaseStructureScreen::new);
    }
}
