package com.victorbrndls.indus.blocks.structure.orientation;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public abstract class BaseIndusStructureOrientation implements IndusStructureOrientation {

    protected final Direction direction;

    private final Vec3 baseOffset;

    public BaseIndusStructureOrientation(
            Direction direction,
            Vec3 offset
    ) {
        this.direction = direction;
        this.baseOffset = offset;
    }

    @Override
    public Vec3 getOffset() {
        return switch (direction) {
            case NORTH -> new Vec3(baseOffset.x + 1, baseOffset.y, baseOffset.z + 1);
            case SOUTH -> new Vec3(-baseOffset.x, baseOffset.y, 0);
            case WEST -> new Vec3(baseOffset.z + 1, baseOffset.y, -baseOffset.x);
            case EAST -> new Vec3(0, baseOffset.y, baseOffset.x + 1);
            default -> Vec3.ZERO;
        };
    }

    @Override
    public void translate(PoseStack ms) {
        switch (direction) {
            case NORTH -> ms.translate(baseOffset.x + 0.5, baseOffset.y, 0);
            case SOUTH -> ms.translate(-baseOffset.x - 0.5, baseOffset.y, 0);
            case WEST -> ms.translate(0, baseOffset.y, -baseOffset.x - 0.5);
            case EAST -> ms.translate(0, baseOffset.y, baseOffset.x + 0.5);
            default -> {
            }
        }
    }

    @Override
    public int rotationDegrees() {
        return switch (direction) {
            case NORTH -> 180;
            case SOUTH -> 0;
            case WEST -> 270;
            case EAST -> 90;
            default -> 0;
        };
    }
}
