package com.victorbrndls.indus.blocks.structure;

import com.victorbrndls.indus.Indus;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.io.IOException;
import java.util.Optional;

public class IndusStructureHelper {

    public static String getResourcePath(IndusStructure structure) {
        return switch (structure) {
            case TREE_FARM -> "tree_farm";
            case QUARRY -> "quarry";
        };
    }

    public static Optional<StructureTemplate> load(MinecraftServer server, IndusStructure structure) {
        var resourcePath = IndusStructureHelper.getResourcePath(structure);

        var resourceManager = server.getResourceManager();
        var blockRegistry = server.registryAccess().lookupOrThrow(Registries.BLOCK);

        var file = ResourceLocation.fromNamespaceAndPath(Indus.MODID, "structures/" + resourcePath + ".nbt");

        return resourceManager.getResource(file).map(res -> {
            try (var in = res.open()) {
                var tag = NbtIo.readCompressed(in, NbtAccounter.unlimitedHeap());
                var tpl = new StructureTemplate();
                tpl.load(blockRegistry, tag);
                return tpl;
            } catch (IOException e) {
                return null;
            }
        });
    }

}
