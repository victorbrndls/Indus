package com.victorbrndls.indus.items;

import net.minecraft.world.item.Item;

public class ConstructionPartItem extends Item {
    private final ConstructionPartTier tier;

    public ConstructionPartItem(ConstructionPartTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public ConstructionPartTier getTier() {
        return tier;
    }
}
