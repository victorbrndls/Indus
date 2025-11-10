package com.victorbrndls.indus.mod.structure.orientation;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class MixerStructureOrientation extends BaseIndusStructureOrientation {

    public MixerStructureOrientation(Direction direction) {
        super(direction, new Vec3(0, -0.99, 0));
    }
}
