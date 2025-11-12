package com.victorbrndls.indus.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class IndusEnergyNetwork {

    public static final Codec<IndusEnergyNetwork> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.LONG.fieldOf("id").forGetter(IndusEnergyNetwork::getId),
            Codec.INT.fieldOf("energy").forGetter(IndusEnergyNetwork::getEnergy),
            Codec.INT.fieldOf("capacity").forGetter(IndusEnergyNetwork::getCapacity)
    ).apply(i, IndusEnergyNetwork::new));

    private long id;
    private int energy;
    private int capacity;

    public IndusEnergyNetwork(long id, int energy, int capacity) {
        this.id = id;
        this.energy = energy;
        this.capacity = capacity;
    }

    public long getId() {
        return id;
    }

    public int getEnergy() {
        return energy;
    }

    public int getCapacity() {
        return capacity;
    }

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
        if (capacity < 0) {
            capacity = 0;
        }

        if (energy > capacity) {
            energy = capacity;
        }
    }
}