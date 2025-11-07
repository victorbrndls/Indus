package com.victorbrndls.indus.mod.structure.orientation;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class TreeFarmStructureOrientation extends BaseIndusStructureOrientation {

    public TreeFarmStructureOrientation(Direction direction) {
        super(direction, new Vec3(1, 0, 0));
    }
}
