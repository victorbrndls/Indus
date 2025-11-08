package com.victorbrndls.indus.mod.structure;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public enum IndusStructure {
    TREE_FARM,
    QUARRY,
    BLAST_FURNACE;

    public static final StreamCodec<FriendlyByteBuf, IndusStructure> CODEC =
            StreamCodec.of(
                    FriendlyByteBuf::writeEnum,
                    buf -> buf.readEnum(IndusStructure.class)
            );
}
