package com.victorbrndls.indus.entities;

import com.victorbrndls.indus.items.IndusItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.MinecartHopper;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.Comparator;

public class ChunkLoaderMinecart extends MinecartHopper {

    public static final TicketType<ChunkPos> TICKET_TYPE = TicketType.create(
            "indus_minecart",
            Comparator.comparingLong(ChunkPos::toLong)
    );

    public ChunkLoaderMinecart(EntityType<? extends MinecartHopper> type, Level level) {
        super(type, level);
    }

    public ChunkLoaderMinecart(Level level, double x, double y, double z) {
        super(IndusEntities.CHUNK_LOADER_MINECART.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    public Item getDropItem() {
        return IndusItems.CHUNK_LOADER_MINECART.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide() && level() instanceof ServerLevel serverLevel) {
            ChunkPos chunkPos = this.chunkPosition();
            serverLevel.getChunkSource().addRegionTicket(TICKET_TYPE, chunkPos, 2, chunkPos);
        }
    }
}
