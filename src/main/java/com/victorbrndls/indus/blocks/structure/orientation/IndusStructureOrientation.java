package com.victorbrndls.indus.blocks.structure.orientation;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.phys.Vec3;

public interface IndusStructureOrientation {

    Vec3 getOffset();

    void translate(PoseStack ms);

    int rotationDegrees();
}
