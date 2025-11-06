package com.victorbrndls.indus.client;

import com.victorbrndls.indus.blocks.structure.IndusStructure;
import com.victorbrndls.indus.blocks.structure.IndusStructureInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndusStructureCache {

    private final Map<IndusStructure, IndusStructureInfo> STRUCTURE_CACHE = new HashMap<>();
    private final List<IndusStructure> REQUESTS_IN_PROGRESS = new ArrayList<>();

    public void add(IndusStructureInfo info) {
        STRUCTURE_CACHE.put(info.structure(), info);
        REQUESTS_IN_PROGRESS.remove(info.structure());
    }

    public IndusStructureInfo get(IndusStructure structure) {
        return STRUCTURE_CACHE.get(structure);
    }

    public void requestMade(IndusStructure structure) {
        REQUESTS_IN_PROGRESS.add(structure);
    }

    public boolean shouldRequest(IndusStructure structure) {
        return !STRUCTURE_CACHE.containsKey(structure) && !REQUESTS_IN_PROGRESS.contains(structure);
    }
}
