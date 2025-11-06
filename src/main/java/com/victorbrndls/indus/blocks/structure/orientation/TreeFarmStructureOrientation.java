package com.victorbrndls.indus.blocks.structure.orientation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.victorbrndls.indus.blocks.structure.IndusStructureOrientation;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

public class TreeFarmStructureOrientation implements IndusStructureOrientation {

    private final Direction direction;

    public TreeFarmStructureOrientation(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Vec3 getCenter() {
        return new Vec3(5, 0, 7);
    }

    @Override
    public Vec3i getOffset() {
        return switch (direction) {
            case NORTH -> new Vec3i(-8, 0, -13);
            case SOUTH -> new Vec3i(-1, 0, 0);
            case WEST -> new Vec3i(-11, 0, -3);
            case EAST -> new Vec3i(2, 0, -10);
            default -> Vec3i.ZERO;
        };
    }

    @Override
    public void translate(PoseStack ms) {
        switch (direction) {
            case NORTH -> {
                ms.translate(-3.5, 0, -6.5);
            }
            case SOUTH -> {
                ms.translate(3.5, 0, 6.5);
            }
            case WEST -> {
                ms.translate(-6.5, 0, 3.5);
            }
            case EAST -> {
                ms.translate(6.5, 0, -3.5);
            }
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
