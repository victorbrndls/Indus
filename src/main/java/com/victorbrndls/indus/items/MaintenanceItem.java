package com.victorbrndls.indus.items;

import net.minecraft.world.item.Item;

public class MaintenanceItem extends Item {
    private final MaintenanceTier tier;

    public MaintenanceItem(MaintenanceTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public MaintenanceTier getTier() {
        return tier;
    }
}
