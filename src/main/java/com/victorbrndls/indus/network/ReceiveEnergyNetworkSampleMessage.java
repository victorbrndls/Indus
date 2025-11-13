package com.victorbrndls.indus.network;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.client.screen.EnergyNetworkScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public record ReceiveEnergyNetworkSampleMessage(
        int energy,
        int capacity
) implements CustomPacketPayload {

    public static final Type<ReceiveEnergyNetworkSampleMessage> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Indus.MODID, "receive_energy_network_sample")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ReceiveEnergyNetworkSampleMessage> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, ReceiveEnergyNetworkSampleMessage::energy,
                    ByteBufCodecs.INT, ReceiveEnergyNetworkSampleMessage::capacity,
                    ReceiveEnergyNetworkSampleMessage::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ReceiveEnergyNetworkSampleMessage message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Screen screen = mc.screen;
            if (screen instanceof EnergyNetworkScreen graph) {
                graph.addSample(message.energy(), message.capacity());
            }
        });
    }

    public static void register(PayloadRegistrar registrar) {
        registrar.playToClient(
                TYPE,
                ReceiveEnergyNetworkSampleMessage.STREAM_CODEC,
                ReceiveEnergyNetworkSampleMessage::handle
        );
    }
}