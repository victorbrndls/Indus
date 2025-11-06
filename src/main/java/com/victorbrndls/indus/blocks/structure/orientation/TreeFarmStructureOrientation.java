package com.victorbrndls.indus.blocks.structure.orientation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.victorbrndls.indus.blocks.structure.IndusStructureOrientation;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.phys.Vec3;

public class TreeFarmStructureOrientation implements IndusStructureOrientation {

    private final Direction direction;

    public TreeFarmStructureOrientation(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void updateSettings(StructurePlaceSettings settings) {
        switch (direction) {
            case NORTH -> settings.setRotation(Rotation.CLOCKWISE_180);
            case SOUTH -> settings.setRotation(Rotation.NONE);
            case WEST -> settings.setRotation(Rotation.CLOCKWISE_90);
            case EAST -> settings.setRotation(Rotation.COUNTERCLOCKWISE_90);
            default -> {
            }
        }
    }

    @Override
    public Vec3 getCenter() {
        return new Vec3(5, 0, 7);
    }

    @Override
    public Vec3 getOffset() {
        return switch (direction) {
            case NORTH -> new Vec3(0, 0, 0);
            case SOUTH -> new Vec3(0, 0, 0);
            case WEST -> new Vec3(0, 0, 0);
            case EAST -> new Vec3(0, 0, 0);
            default -> Vec3.ZERO;
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
    public double rotationDegrees() {
        return switch (direction) {
            case NORTH -> 180;
            case SOUTH -> 0;
            case WEST -> 270;
            case EAST -> 90;
            default -> 0;
        };
    }
}
