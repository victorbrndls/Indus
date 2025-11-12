package com.victorbrndls.indus.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class IndusEnergyNetwork {

    public static final Codec<IndusEnergyNetwork> NET_CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("energy").forGetter(IndusEnergyNetwork::getEnergy),
            Codec.INT.fieldOf("capacity").forGetter(IndusEnergyNetwork::getCapacity)
    ).apply(i, IndusEnergyNetwork::new));

    private int energy;
    private int capacity;

    public IndusEnergyNetwork(int energy, int capacity) {
        this.energy = energy;
        this.capacity = capacity;
    }

    public int getEnergy() {
        return energy;
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * @return the amount of energy actually added
     */
    public int addEnergy(int v) {
        int accepted = Math.min(v, capacity - energy);
        energy += accepted;
        return accepted;
    }

    public boolean consumeEnergy(int v) {
        if (energy < v) return false;
        energy -= v;
        return true;
    }

    public void addCapacity(int v) {
        capacity += v;
    }

    public void removeCapacity(int v) {
        capacity -= v;
        if (energy > capacity) {
            energy = capacity;
        }
    }
}