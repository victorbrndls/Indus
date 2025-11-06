package com.victorbrndls.indus.blocks.structure;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.phys.Vec3;

public interface IndusStructureOrientation {

    public void updateSettings(StructurePlaceSettings settings);

    public Vec3 getOffset();

    public Vec3 getCenter();

    void translate(PoseStack ms);

    double rotationDegrees();
}
