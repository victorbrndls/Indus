package com.victorbrndls.indus.blocks.structure.orientation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.victorbrndls.indus.blocks.structure.IndusStructureOrientation;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class QuarryStructureOrientation implements IndusStructureOrientation {

    private final Direction direction;

    public QuarryStructureOrientation(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Vec3 getOffset() {
        return switch (direction) {
            case NORTH -> new Vec3(2, 0, 1);
            case SOUTH -> new Vec3(-1, 0, 0);
            case WEST -> new Vec3(1, 0, -1);
            case EAST -> new Vec3(0, 0, 2);
            default -> Vec3.ZERO;
        };
    }

    @Override
    public void translate(PoseStack ms) {
        switch (direction) {
            case NORTH -> ms.translate(1.5, 0, 0);
            case SOUTH -> ms.translate(-1.5, 0, 0);
            case WEST -> ms.translate(0, 0, -1.5);
            case EAST -> ms.translate(0, 0, 1.5);
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
