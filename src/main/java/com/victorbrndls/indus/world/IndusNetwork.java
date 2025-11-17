package com.victorbrndls.indus.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class IndusNetwork {

    public static final Codec<IndusNetwork> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.LONG.fieldOf("id").forGetter(IndusNetwork::getId),
            Codec.INT.fieldOf("energy").forGetter(IndusNetwork::getEnergy),
            Codec.INT.fieldOf("maintenance1").forGetter(IndusNetwork::getMaintenance1)
    ).apply(i, IndusNetwork::new));

    private static final int MAINTENANCE_CAPACITY = 1000;

    private final long id;

    private int energy;
    private int energyCapacity;

    private int maintenance1;

    public IndusNetwork(long id, int energy, int maintenance1) {
        this.id = id;

        this.energy = energy;
        this.energyCapacity = 0;

        this.maintenance1 = maintenance1;
    }

    public static IndusNetwork newNetwork(long id) {
        return new IndusNetwork(id, 0, MAINTENANCE_CAPACITY);
    }

    public long getId() {
        return id;
    }

    public int getEnergy() {
        return energy;
    }

    public int getEnergyCapacity() {
        return energyCapacity;
    }

    public int addEnergy(int v) {
        int accepted = Math.min(v, energyCapacity - energy);
        energy += accepted;
        return accepted;
    }

    public boolean consumeEnergy(int v) {
        if (energy < v) return false;
        energy -= v;
        return true;
    }

    public void addEnergyCapacity(int v) {
        energyCapacity += v;
    }

    public void removeEnergyCapacity(int v) {
        energyCapacity -= v;
        if (energyCapacity < 0) {
            energyCapacity = 0;
        }

        if (energy > energyCapacity) {
            energy = energyCapacity;
        }
    }

    public int getMaintenance1() {
        return maintenance1;
    }

    public int addMaintenance1(int v) {
        int accepted = Math.min(v, MAINTENANCE_CAPACITY - maintenance1);
        maintenance1 += accepted;
        return accepted;
    }

    public boolean consumeMaintenance1(int v) {
        if (maintenance1 < v) return false;
        maintenance1 -= v;
        return true;
    }

    public int getMaintenanceCapacity() {
        return MAINTENANCE_CAPACITY;
    }
}