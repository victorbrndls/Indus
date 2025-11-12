package com.victorbrndls.indus.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.victorbrndls.indus.Indus;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;

public class IndusEnergyManager extends SavedData {

    public static final Codec<IndusEnergyManager> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                    Codec.LONG.fieldOf("nextId").forGetter(d -> d.nextId),
                    Codec.list(IndusEnergyNetwork.CODEC).fieldOf("networks")
                            .forGetter(m -> new ArrayList<>(m.networks.values()))
            ).apply(i, (nextId, list) -> {
                var map = new Long2ObjectOpenHashMap<IndusEnergyNetwork>(list.size());
                for (var network : list) map.put(network.getId(), network);
                return new IndusEnergyManager(nextId, map);
            })
    );

    public static final SavedDataType<IndusEnergyManager> ID = new SavedDataType<>(
            "indus/energy",
            () -> new IndusEnergyManager(1, new Long2ObjectOpenHashMap<>()),
            CODEC
    );

    private final Long2ObjectOpenHashMap<IndusEnergyNetwork> networks = new Long2ObjectOpenHashMap<>();

    private long nextId;

    private IndusEnergyManager(long nextId, Long2ObjectOpenHashMap<IndusEnergyNetwork> networks) {
        this.nextId = nextId;
        this.networks.putAll(networks);
    }

    public static IndusEnergyManager get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(ID);
    }

    public long getNetworkId() {
        return nextId++;
    }

    public IndusEnergyNetwork getNetwork(long id) {
        var network = networks.get(id);
        if (network == null) {
            network = new IndusEnergyNetwork(id, 0, 0);
            networks.put(id, network);
        }

        return network;
    }

    public int addEnergy(long id, int v) {
        var added = getNetwork(id).addEnergy(v);
        if (added > 0) {
            setDirty();
        }
        return added;
    }

    public boolean consumeEnergy(long id, int v) {
        var consumed = getNetwork(id).consumeEnergy(v);
        if (consumed) {
            setDirty();
        }
        return consumed;
    }

    public void addCapacity(long id, int v) {
        getNetwork(id).addCapacity(v);
        Indus.LOGGER.debug("Added capacity to network {}: {}", id, v);
        setDirty();
    }

    public void removeCapacity(long id, int v) {
        getNetwork(id).removeCapacity(v);
        Indus.LOGGER.debug("Removed capacity from network {}: {}", id, v);
        setDirty();
    }

}