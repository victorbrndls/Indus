package com.victorbrndls.indus.network;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.mod.structure.IndusStructureInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.List;

public record ReceiveStructureMessage(
        IndusStructure structure,
        List<BlockPos> positions,
        List<BlockState> states
) implements CustomPacketPayload {

    public static final Type<ReceiveStructureMessage> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Indus.MODID, "receive_structure")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ReceiveStructureMessage> STREAM_CODEC =
            StreamCodec.composite(
                    IndusStructure.CODEC, ReceiveStructureMessage::structure,
                    BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()), ReceiveStructureMessage::positions,
                    ByteBufCodecs.fromCodec(BlockState.CODEC).apply(ByteBufCodecs.list()), ReceiveStructureMessage::states,
                    ReceiveStructureMessage::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ReceiveStructureMessage message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Indus.LOGGER.debug("Received structure: {}", message.structure.name());
            Indus.STRUCTURE_CACHE.add(
                    new IndusStructureInfo(message.structure(), message.positions(), message.states)
            );
        });
    }

    public static void register(PayloadRegistrar registrar) {
        registrar.playToClient(
                TYPE,
                ReceiveStructureMessage.STREAM_CODEC,
                ReceiveStructureMessage::handle
        );
    }
}