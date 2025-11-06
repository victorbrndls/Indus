package com.victorbrndls.indus.network;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.blocks.structure.IndusStructure;
import com.victorbrndls.indus.blocks.structure.IndusStructureHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.ArrayList;

public record RequestStructureMessage(
        IndusStructure structure
) implements CustomPacketPayload {

    public static final Type<RequestStructureMessage> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Indus.MODID, "request_structure")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, RequestStructureMessage> STREAM_CODEC =
            StreamCodec.composite(
                    IndusStructure.CODEC, RequestStructureMessage::structure,
                    RequestStructureMessage::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(RequestStructureMessage message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var sender = (ServerPlayer) ctx.player();
            var level = sender.level();

            IndusStructureHelper.load(level.getServer(), message.structure)
                    .ifPresent(struct -> {
                        var palette = struct.palettes.getFirst();

                        var blocks = new ArrayList<>(palette.blocks());
                        blocks.removeIf(blockInfo -> blockInfo.state().isAir());

                        var pos = blocks.stream().map(StructureTemplate.StructureBlockInfo::pos).toList();
                        var state = blocks.stream().map(StructureTemplate.StructureBlockInfo::state).toList();

                        PacketDistributor.sendToPlayer(
                                sender,
                                new ReceiveStructureMessage(message.structure, pos, state)
                        );
                    });
        });
    }

    public static void register(PayloadRegistrar registrar) {
        registrar.playToServer(
                TYPE,
                RequestStructureMessage.STREAM_CODEC,
                RequestStructureMessage::handle
        );
    }
}