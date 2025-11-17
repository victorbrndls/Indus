package com.victorbrndls.indus.items;

public enum MaintenanceTier {
    BASIC(1),
    ADVANCED(2),
    ELITE(3);

    private final int tierLevel;

    MaintenanceTier(int tierLevel) {
        this.tierLevel = tierLevel;
    }

    public int getTierLevel() {
        return tierLevel;
    }
}
