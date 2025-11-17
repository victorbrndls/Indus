package com.victorbrndls.indus.network;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.world.IndusNetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public record RequestNetworkSampleMessage(
        long networkId
) implements CustomPacketPayload {

    public static final Type<RequestNetworkSampleMessage> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Indus.MODID, "request_network_sample")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, RequestNetworkSampleMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG, RequestNetworkSampleMessage::networkId,
            RequestNetworkSampleMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(RequestNetworkSampleMessage message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var sender = (ServerPlayer) ctx.player();
            var level = sender.level();

            var network = IndusNetworkManager.get(level).getNetwork(message.networkId);

            PacketDistributor.sendToPlayer(
                    sender,
                    new ReceiveNetworkSampleMessage(
                            network.getEnergy(),
                            network.getEnergyCapacity(),
                            network.getMaintenance1()
                    )
            );
        });
    }

    public static void register(PayloadRegistrar registrar) {
        registrar.playToServer(
                TYPE,
                RequestNetworkSampleMessage.STREAM_CODEC,
                RequestNetworkSampleMessage::handle
        );
    }
}