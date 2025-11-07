package com.victorbrndls.indus.gui;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.blocks.tileentity.BaseStructureBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IndusMenus {

    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, Indus.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<BaseStructureMenu>> BASE_STRUCTURE = MENUS.register("base_structure",
            () -> new MenuType<>((IContainerFactory<BaseStructureMenu>) (id, inventory, buf) -> {
                var pos = buf.readBlockPos();
                var blockEntity = inventory.player.level().getBlockEntity(pos);
                if (blockEntity instanceof BaseStructureBlockEntity entity) {
                    return new BaseStructureMenu(id, inventory, entity);
                }
                return null;
            }, FeatureFlags.DEFAULT_FLAGS));

    public static void init(IEventBus eventBus) {
        MENUS.register(eventBus);
    }

}
