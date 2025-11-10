package com.victorbrndls.indus.items;

public enum ConstructionPartTier {
    BASIC(1),
    ADVANCED(2),
    ELITE(3),
    ULTIMATE(4);

    private final int tierLevel;

    ConstructionPartTier(int tierLevel) {
        this.tierLevel = tierLevel;
    }

    public int getTierLevel() {
        return tierLevel;
    }
}
