package com.victorbrndls.indus.blocks.structure.orientation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.victorbrndls.indus.blocks.structure.IndusStructureOrientation;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class TreeFarmStructureOrientation implements IndusStructureOrientation {

    private final Direction direction;

    private final Vec3 offset = new Vec3(1, 0, 0);

    public TreeFarmStructureOrientation(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Vec3 getOffset() {
        return switch (direction) {
            case NORTH -> new Vec3(offset.x + 1, offset.y, offset.z + 1);
            case SOUTH -> new Vec3(-offset.x, offset.y, 0);
            case WEST -> new Vec3(offset.z + 1, offset.y, -offset.x);
            case EAST -> new Vec3(0, offset.y, offset.x + 1);
            default -> Vec3.ZERO;
        };
    }

    @Override
    public void translate(PoseStack ms) {
        switch (direction) {
            case NORTH -> ms.translate(offset.x + 0.5, offset.y, 0);
            case SOUTH -> ms.translate(-offset.x - 0.5, offset.y, 0);
            case WEST -> ms.translate(0, offset.y, -offset.x - 0.5);
            case EAST -> ms.translate(0, offset.y, offset.x + 0.5);
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
