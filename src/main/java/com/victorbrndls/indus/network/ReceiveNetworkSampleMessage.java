package com.victorbrndls.indus.network;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.client.screen.IndusNetworkScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public record ReceiveNetworkSampleMessage(
        int energy,
        int energyCapacity,
        int maintenance1
) implements CustomPacketPayload {

    public static final Type<ReceiveNetworkSampleMessage> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Indus.MODID, "receive_network_sample")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ReceiveNetworkSampleMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ReceiveNetworkSampleMessage::energy,
            ByteBufCodecs.INT, ReceiveNetworkSampleMessage::energyCapacity,
            ByteBufCodecs.INT, ReceiveNetworkSampleMessage::maintenance1,
            ReceiveNetworkSampleMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ReceiveNetworkSampleMessage message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Screen screen = mc.screen;
            if (screen instanceof IndusNetworkScreen graph) {
                graph.addSample(message.energy(), message.energyCapacity(), message.maintenance1());
            }
        });
    }

    public static void register(PayloadRegistrar registrar) {
        registrar.playToClient(
                TYPE,
                ReceiveNetworkSampleMessage.STREAM_CODEC,
                ReceiveNetworkSampleMessage::handle
        );
    }
}