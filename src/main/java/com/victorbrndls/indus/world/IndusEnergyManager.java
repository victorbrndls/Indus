package com.victorbrndls.indus.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class IndusEnergyManager extends SavedData {

    public static final Codec<IndusEnergyManager> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                            Codec.LONG.fieldOf("nextId").forGetter(d -> d.nextId),
                            Codec.unboundedMap(Codec.LONG, IndusEnergyNetwork.NET_CODEC)
                                    .xmap(Long2ObjectOpenHashMap::new, Object2ObjectOpenHashMap::new)
                                    .fieldOf("networks").forGetter(d -> d.networks)
                    )
                    .apply(i, IndusEnergyManager::new)
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
            network = new IndusEnergyNetwork(0, 0);
            networks.put(id, network);
        }

        return network;
    }

    public int addEnergy(long id, int v) {
        var added = getNetwork(id).addEnergy(v);
        setDirty();
        return added;
    }

    public boolean consumeEnergy(long id, int v) {
        var consumed = getNetwork(id).consumeEnergy(v);
        if (consumed) {
            setDirty();
        }
        return consumed;
    }


}