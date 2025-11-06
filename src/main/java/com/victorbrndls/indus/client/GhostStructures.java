package com.victorbrndls.indus.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.victorbrndls.indus.IndusClient;
import com.victorbrndls.indus.blocks.structure.IndusStructure;
import com.victorbrndls.indus.blocks.structure.IndusStructureHelper;
import com.victorbrndls.indus.blocks.structure.IndusStructureOrientation;
import com.victorbrndls.indus.items.IndusStructureItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.EmptyBlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

import static net.neoforged.neoforge.client.RenderTypeHelper.getEntityRenderType;

public class GhostStructures {

    private final List<IndusStructure> ghosts = new ArrayList<>();

    private static final RandomSource RANDOM = RandomSource.create();

    public void tick() {
        Item mainHandItem = Minecraft.getInstance().player.getMainHandItem().getItem();
        if (!(mainHandItem instanceof IndusStructureItem structureItem)) {
            if (!ghosts.isEmpty()) {
                ghosts.clear();
            }
            return;
        }

        IndusStructure structure = structureItem.getStructure();

        if (ghosts.contains(structure)) return;

        ghosts.clear();
        ghosts.add(structure);

        IndusStructureHelper.requestStructure(structure);
    }

    public void add(IndusStructure entry) {
        ghosts.add(entry);
    }

    public boolean shouldRender() {
        return !ghosts.isEmpty();
    }

    public void renderAll(PoseStack ms, MultiBufferSource buffer, Vec3 camera, Direction direction) {
        ghosts.forEach(structure -> {
            render(ms, buffer, camera, direction, structure);
        });
    }

    private void render(PoseStack ms, MultiBufferSource buffer, Vec3 camera, Direction direction, IndusStructure structure) {
        var structureInfo = IndusClient.STRUCTURE_CACHE.get(structure);
        if (structureInfo == null) return;

        var blocks = structureInfo.pos();
        var states = structureInfo.blockState();

        var orientation = IndusStructureHelper.getOrientation(structure, direction);

        for (int i = 0; i < blocks.size(); i++) {
            render(ms, buffer, camera, blocks.get(i), states.get(i), orientation);
        }
    }

    private void render(
            PoseStack ms,
            MultiBufferSource buffer,
            Vec3 camera,
            BlockPos blockPos,
            BlockState blockState,
            IndusStructureOrientation orientation
    ) {
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

        HitResult hitResult = Minecraft.getInstance().hitResult;
        if (hitResult == null) return;

        Vec3 blockPosVec = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());

        BlockStateModel model = dispatcher.getBlockModel(blockState);

        ms.pushPose();

        orientation.translate(ms);

        var angleRadians = (float) Math.toRadians(orientation.rotationDegrees());
        var blockPosAfterRotation = blockPosVec.yRot(angleRadians);

        Vec3 blockPosInWorld = hitResult.getLocation().add(
                blockPosAfterRotation.x, blockPosAfterRotation.y, blockPosAfterRotation.z
        );

        ms.translate(
                blockPosInWorld.x() - camera.x, blockPosInWorld.y() - camera.y, blockPosInWorld.z() - camera.z
        );

        var rotationQuaternion = new Quaternionf(0, (float) Math.sin(angleRadians / 2), 0, (float) Math.cos(angleRadians / 2));
        ms.mulPose(rotationQuaternion);

        renderModel(
                blockState,
                EmptyBlockAndTintGetter.INSTANCE,
                blockPos,
                ms.last(),
                buffer,
                model,
                RANDOM,
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY
        );

        ms.popPose();
    }

    public static void renderModel(
            BlockState state,
            BlockAndTintGetter level,
            BlockPos pos,
            PoseStack.Pose pose,
            MultiBufferSource bufferSource,
            BlockStateModel model,
            RandomSource random,
            int light,
            int overlay
    ) {
        for (BlockModelPart part : model.collectParts(level, pos, state, random)) {
            VertexConsumer buffer = bufferSource.getBuffer(getEntityRenderType(part.getRenderType(state)));
            for (Direction side : Direction.values()) {
                renderQuadList(state, level, pos, pose, buffer, part.getQuads(side), light, overlay);
            }

            renderQuadList(state, level, pos, pose, buffer, part.getQuads(null), light, overlay);
        }
    }

    public static void renderQuadList(
            BlockState state,
            BlockAndTintGetter level,
            BlockPos pos,
            PoseStack.Pose pose,
            VertexConsumer consumer,
            List<BakedQuad> quads,
            int light,
            int overlay
    ) {
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        int lastTintIndex = -1;
        int lastTintValue = 0xFFFFFFFF;

        for (BakedQuad quad : quads) {
            float redF = 1.0F;
            float greenF = 1.0F;
            float blueF = 1.0F;
            if (quad.isTinted()) {
                int color = lastTintValue;
                if (lastTintIndex != quad.tintIndex()) {
                    lastTintIndex = quad.tintIndex();
                    color = lastTintValue = blockColors.getColor(state, level, pos, lastTintIndex);
                }
                redF = ARGB.redFloat(color);
                greenF = ARGB.greenFloat(color);
                blueF = ARGB.blueFloat(color);
            }

            consumer.putBulkData(pose, quad, redF, greenF, blueF, 0.7F, light, overlay, true);
        }
    }

}
