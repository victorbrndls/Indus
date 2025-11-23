package com.victorbrndls.indus.shared;

import com.victorbrndls.indus.items.IndusItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class FluidLocator {

    @Nullable
    public static Item prospect(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel sl)) return null;

        long seed = mixedSeed(sl, pos);
        float random = RandomSource.create(seed).nextFloat();

        if (sl.dimension() == Level.OVERWORLD) {
            if (random < 0.35f) return IndusItems.WATER_CELL.get();
            if (random < 0.45f) return IndusItems.CRUDE_OIL_CELL.get();
        }

        return null;
    }

    // Seed is stable per chunk and per dimension
    private static long mixedSeed(ServerLevel sl, BlockPos pos) {
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;

        long worldSeed = sl.getSeed();
        int dimHash = sl.dimension().location().toString().hashCode();

        long h = worldSeed;
        h ^= 0x4A7C159E3779B97FL * chunkX;
        h = Long.rotateLeft(h, 27);
        h ^= 0xD4EB4FC2B2AE3D27L * chunkZ;
        h = Long.rotateLeft(h, 31);
        h ^= (long) dimHash * 0x94D049BB133111EBL;

        // final mix
        h ^= (h >>> 33);
        h *= 0xff51afd7ed558ccdL;
        h ^= (h >>> 33);
        h *= 0xc4ceb9fe1a85ec53L;
        h ^= (h >>> 33);
        return h;
    }

}
