package com.victorbrndls.indus.mod.structure;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.mod.structure.orientation.*;
import com.victorbrndls.indus.network.RequestStructureMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class IndusStructureHelper {

    public static String getResourcePath(IndusStructure structure) {
        return switch (structure) {
            case TREE_FARM -> "tree_farm";
            case QUARRY -> "quarry";
            case BLAST_FURNACE -> "blast_furnace";
            case MIXER -> "mixer";
            case PUMP -> "pump";
            case STEAM_GENERATOR -> "steam_generator";
            case CRUSHER -> "crusher";
            case ASSEMBLER_1 -> "assembler_1";
        };
    }

    public static Optional<StructureTemplate> loadTemplate(MinecraftServer server, IndusStructure structure) {
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

    public static Optional<IndusStructureInfo> loadStructureInfo(MinecraftServer server, IndusStructure structure) {
        var maybeTemplate = loadTemplate(server, structure);
        if (maybeTemplate.isEmpty()) return Optional.empty();

        var template = maybeTemplate.get();
        var palette = template.palettes.getFirst();

        var sorted = new ArrayList<>(palette.blocks()).stream()
                .sorted(Comparator.comparing(StructureTemplate.StructureBlockInfo::pos,
                        Comparator.<BlockPos>comparingInt(BlockPos::getY)
                                .thenComparingInt(BlockPos::getZ)
                                .thenComparingInt(BlockPos::getX)))
                .toList();

        var pos = sorted.stream().map(StructureTemplate.StructureBlockInfo::pos).toList();
        var state = sorted.stream().map(StructureTemplate.StructureBlockInfo::state).toList();

        var info = new IndusStructureInfo(structure, pos, state);
        Indus.STRUCTURE_CACHE.add(info);

        return Optional.of(info);
    }

    @Nullable
    public static IndusStructureInfo getOrLoadStructureInfo(MinecraftServer server, IndusStructure structure) {
        var cached = Indus.STRUCTURE_CACHE.get(structure);
        if (cached != null) return cached;
        return loadStructureInfo(server, structure).orElse(null);
    }

    // client side only
    public static void requestStructure(IndusStructure structure) {
        if (!Indus.STRUCTURE_CACHE.shouldRequest(structure)) return;
        Indus.STRUCTURE_CACHE.onRequestMade(structure);
        ClientPacketDistributor.sendToServer(new RequestStructureMessage(structure));
    }

    public static IndusStructureOrientation getOrientation(IndusStructure structure, Direction direction) {
        return switch (structure) {
            case TREE_FARM -> new TreeFarmStructureOrientation(direction);
            case QUARRY -> new QuarryStructureOrientation(direction);
            case BLAST_FURNACE -> new BlastFurnaceStructureOrientation(direction);
            case MIXER -> new MixerStructureOrientation(direction);
            case PUMP -> new PumpStructureOrientation(direction);
            case STEAM_GENERATOR -> new SteamGeneratorStructureOrientation(direction);
            case CRUSHER -> new CrusherStructureOrientation(direction);
            case ASSEMBLER_1 -> new Assembler1StructureOrientation(direction);
        };
    }

}
