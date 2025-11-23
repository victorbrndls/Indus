package com.victorbrndls.indus.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.items.IndusStructureItem;
import com.victorbrndls.indus.mod.structure.IndusStructure;
import com.victorbrndls.indus.mod.structure.IndusStructureHelper;
import com.victorbrndls.indus.mod.structure.orientation.IndusStructureOrientation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.apache.commons.lang3.tuple.Triple;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GhostStructures {

    private final List<IndusStructure> ghosts = new ArrayList<>();

    private final List<Triple<BlockPos, Direction, IndusStructure>> fixedGhosts = new ArrayList<>();

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

    public void toggleFixedGhost(BlockPos pos, Direction direction, IndusStructure structure) {
        var pair = Triple.of(pos, direction, structure);
        if (fixedGhosts.contains(pair)) {
            fixedGhosts.remove(pair);
        } else {
            fixedGhosts.add(pair);
        }
    }

    public void removeFixedGhost(BlockPos pos) {
        fixedGhosts.removeIf(triple -> triple.getLeft().equals(pos));
    }

    public boolean shouldRender() {
        return !ghosts.isEmpty() || !fixedGhosts.isEmpty();
    }

    public void renderAll(PoseStack ms, MultiBufferSource buffer, Vec3 camera, Direction direction) {
        HitResult hitResult = Minecraft.getInstance().hitResult;
        if (hitResult != null) {
            ghosts.forEach(structure -> {
                render(ms, buffer, camera, hitResult.getLocation(), direction, structure);
            });
        }

        fixedGhosts.forEach(triple -> {
            BlockPos blockPos = triple.getLeft();

            Vec3 hit = switch (triple.getMiddle()) {
                case NORTH -> new Vec3(blockPos.getX() + 0.5f, blockPos.getY(), blockPos.getZ() + 1f);
                case SOUTH -> new Vec3(blockPos.getX() + 0.5f, blockPos.getY(), blockPos.getZ());
                case WEST -> new Vec3(blockPos.getX() + 1f, blockPos.getY(), blockPos.getZ() + 0.5f);
                case EAST -> new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ() + 0.5f);
                case DOWN, UP -> new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            };

            render(ms, buffer, camera, hit, triple.getMiddle(), triple.getRight());
        });
    }

    private void render(PoseStack ms, MultiBufferSource buffer, Vec3 camera, Vec3 hit, Direction direction, IndusStructure structure) {
        var structureInfo = Indus.STRUCTURE_CACHE.get(structure);
        if (structureInfo == null) return;

        var blocks = structureInfo.pos();
        var states = structureInfo.blockState();

        var orientation = IndusStructureHelper.getOrientation(structure, direction);

        for (int i = 0; i < blocks.size(); i++) {
            render(ms, buffer, camera, hit, blocks.get(i), states.get(i), orientation);
        }
    }

    private void render(
            PoseStack ms,
            MultiBufferSource buffer,
            Vec3 camera,
            Vec3 hit,
            BlockPos blockPos,
            BlockState blockState,
            IndusStructureOrientation orientation
    ) {
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

        Vec3 blockPosVec = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());

        BakedModel model = dispatcher.getBlockModel(blockState);

        ms.pushPose();

        orientation.translate(ms);

        var angleRadians = (float) Math.toRadians(orientation.rotationDegrees());
        var blockPosAfterRotation = blockPosVec.yRot(angleRadians);

        Vec3 blockPosInWorld = hit.add(
                blockPosAfterRotation.x, blockPosAfterRotation.y, blockPosAfterRotation.z
        );

        ms.translate(
                blockPosInWorld.x() - camera.x, blockPosInWorld.y() - camera.y, blockPosInWorld.z() - camera.z
        );

        var rotationQuaternion = new Quaternionf(0, (float) Math.sin(angleRadians / 2), 0, (float) Math.cos(angleRadians / 2));
        ms.mulPose(rotationQuaternion);

        VertexConsumer vb = buffer.getBuffer(RenderType.translucent());
        renderModel(
                ms.last(),
                vb,
                blockState,
                model,
                1f,
                1f,
                1f,
                0.7f,
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY,
                ModelData.EMPTY,
                RenderType.translucent()
        );

        ms.popPose();
    }

    public void renderModel(PoseStack.Pose pose, VertexConsumer consumer,
                            @Nullable BlockState state, BakedModel model, float red, float green, float blue,
                            float alpha, int packedLight, int packedOverlay, ModelData modelData, RenderType renderType) {
        RandomSource random = RandomSource.create();

        for (Direction direction : Direction.values()) {
            random.setSeed(42L);
            renderQuadList(pose, consumer, red, green, blue, alpha,
                    model.getQuads(state, direction, random, modelData, null), packedLight, packedOverlay);
        }

        random.setSeed(42L);
        renderQuadList(pose, consumer, red, green, blue, alpha,
                model.getQuads(state, null, random, modelData, null), packedLight, packedOverlay);
    }

    private static void renderQuadList(PoseStack.Pose pose, VertexConsumer consumer,
                                       float red, float green, float blue, float alpha, List<BakedQuad> quads,
                                       int packedLight, int packedOverlay) {
        for (BakedQuad quad : quads) {
            float f;
            float f1;
            float f2;
            if (quad.isTinted()) {
                f = Mth.clamp(red, 0.0F, 1.0F);
                f1 = Mth.clamp(green, 0.0F, 1.0F);
                f2 = Mth.clamp(blue, 0.0F, 1.0F);
            } else {
                f = 1.0F;
                f1 = 1.0F;
                f2 = 1.0F;
            }

            consumer.putBulkData(pose, quad, f, f1, f2, alpha, packedLight, packedOverlay, true);
        }
    }

}
