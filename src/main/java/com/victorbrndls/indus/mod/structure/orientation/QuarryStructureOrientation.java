package com.victorbrndls.indus.mod.structure.orientation;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class QuarryStructureOrientation extends BaseIndusStructureOrientation {

    public QuarryStructureOrientation(Direction direction) {
        super(direction, new Vec3(0, -7.99, 0));
    }
}
