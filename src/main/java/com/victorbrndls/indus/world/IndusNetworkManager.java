package com.victorbrndls.indus.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.items.MaintenanceTier;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;

public class IndusNetworkManager extends SavedData {

    public static final Codec<IndusNetworkManager> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                    Codec.LONG.fieldOf("nextId").forGetter(d -> d.nextId),
                    Codec.list(IndusNetwork.CODEC).fieldOf("networks")
                            .forGetter(m -> new ArrayList<>(m.networks.values()))
            ).apply(i, (nextId, list) -> {
                var map = new Long2ObjectOpenHashMap<IndusNetwork>(list.size());
                for (var network : list) map.put(network.getId(), network);
                return new IndusNetworkManager(nextId, map);
            })
    );

    public static final SavedDataType<IndusNetworkManager> ID = new SavedDataType<>(
            "indus/energy",
            () -> new IndusNetworkManager(1, new Long2ObjectOpenHashMap<>()),
            CODEC
    );

    private final Long2ObjectOpenHashMap<IndusNetwork> networks = new Long2ObjectOpenHashMap<>();

    private long nextId;

    private IndusNetworkManager(long nextId, Long2ObjectOpenHashMap<IndusNetwork> networks) {
        this.nextId = nextId;
        this.networks.putAll(networks);
    }

    public static IndusNetworkManager get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(ID);
    }

    public long getNetworkId() {
        return nextId++;
    }

    public IndusNetwork getNetwork(long id) {
        var network = networks.get(id);
        if (network == null) {
            network = IndusNetwork.newNetwork(id);
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

    public void addEnergyCapacity(long id, int v) {
        getNetwork(id).addEnergyCapacity(v);
        Indus.LOGGER.debug("Added capacity to network {}: {}", id, v);
        setDirty();
    }

    public void removeEnergyCapacity(long id, int v) {
        getNetwork(id).removeEnergyCapacity(v);
        Indus.LOGGER.debug("Removed capacity from network {}: {}", id, v);
        setDirty();
    }


    public int addMaintenance(long id, MaintenanceTier tier, int v) {
        var network = getNetwork(id);
        int added = 0;

        if (tier == MaintenanceTier.BASIC) {
            added = network.addMaintenance1(v);
        }

        if (added > 0) {
            setDirty();
        }
        return added;
    }

    public boolean consumeMaintenance(long id, MaintenanceTier tier, int v) {
        var network = getNetwork(id);
        boolean consumed = false;

        if (tier == MaintenanceTier.BASIC) {
            consumed = network.consumeMaintenance1(v);
        }

        if (consumed) {
            setDirty();
        }
        return consumed;
    }

    public float getRemainingMaintenancePercentage(long id, MaintenanceTier tier) {
        var network = getNetwork(id);
        float percentage = 0f;

        if (tier == MaintenanceTier.BASIC) {
            percentage = (float) network.getMaintenance1() / (float) network.getMaintenanceCapacity();
        }

        return percentage;
    }

    public int getAvailableMaintenanceSpace(long id, MaintenanceTier tier) {
        var network = getNetwork(id);

        if (tier == MaintenanceTier.BASIC) {
            return network.getMaintenanceCapacity() - network.getMaintenance1();
        }

        return 0;
    }

}