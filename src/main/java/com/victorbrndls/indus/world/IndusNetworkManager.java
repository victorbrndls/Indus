package com.victorbrndls.indus.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.items.MaintenanceTier;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

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


    private final Long2ObjectOpenHashMap<IndusNetwork> networks = new Long2ObjectOpenHashMap<>();

    private long nextId = 1;

    private static final String DATA_TAG = "indus.network";

    private IndusNetworkManager() {
    }

    private IndusNetworkManager(long nextId, Long2ObjectOpenHashMap<IndusNetwork> networks) {
        this.nextId = nextId;
        this.networks.putAll(networks);
    }

    public static IndusNetworkManager get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new SavedData.Factory<>(IndusNetworkManager::new, (tag, registries) -> {
            var manager = new IndusNetworkManager();
            manager.load(tag);
            return manager;
        }), DATA_TAG);
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

    public int getEnergy(long id) {
        return getNetwork(id).getEnergy();
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
        Indus.LOGGER.debug("Added energyCapacity to network {}: {}", id, v);
        setDirty();
    }

    public void removeEnergyCapacity(long id, int v) {
        getNetwork(id).removeEnergyCapacity(v);
        Indus.LOGGER.debug("Removed energyCapacity from network {}: {}", id, v);
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

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        return CODEC.encodeStart(NbtOps.INSTANCE, this)
                .resultOrPartial(err -> Indus.LOGGER.error("Failed to save IndusNetworkManager: {}", err))
                .filter(CompoundTag.class::isInstance)
                .map(CompoundTag.class::cast)
                .map(encoded -> {
                    encoded.merge(encoded);
                    return encoded;
                })
                .orElse(compoundTag);
    }

    private void load(CompoundTag tag) {
        CODEC.decode(NbtOps.INSTANCE, tag)
                .resultOrPartial(err -> Indus.LOGGER.error("Failed to load IndusNetworkManager: {}", err))
                .ifPresent(pair -> {
                    IndusNetworkManager loaded = pair.getFirst();
                    this.nextId = loaded.nextId;
                    this.networks.clear();
                    this.networks.putAll(loaded.networks);
                });
    }
}