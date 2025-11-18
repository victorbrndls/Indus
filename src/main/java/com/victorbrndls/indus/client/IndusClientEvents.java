package com.victorbrndls.indus.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.victorbrndls.indus.IndusClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(Dist.CLIENT)
public class IndusClientEvents {

    @SubscribeEvent
    public static void onTick(ClientTickEvent.Post event) {
        if (!isGameActive()) return;
        IndusClient.GHOST_STRUCTURES.tick();
    }

    @SubscribeEvent
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
        if (!IndusClient.GHOST_STRUCTURES.shouldRender()) return;

        PoseStack ms = event.getPoseStack();
        ms.pushPose();

        var buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        var direction = Minecraft.getInstance().player.getDirection();

        IndusClient.GHOST_STRUCTURES.renderAll(ms, buffer, camera, direction);

        buffer.endLastBatch();
        ms.popPose();
    }

    private static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }

}