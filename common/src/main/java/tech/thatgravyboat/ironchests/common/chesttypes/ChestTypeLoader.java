package tech.thatgravyboat.ironchests.common.chesttypes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.teamresourceful.resourcefullib.common.utils.FileUtils;
import net.minecraft.util.GsonHelper;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;
import tech.thatgravyboat.ironchests.api.chesttype.ChestUpgradeType;
import tech.thatgravyboat.ironchests.common.registry.custom.ChestTypeRegistry;
import tech.thatgravyboat.ironchests.common.registry.custom.ChestUpgradeTypeRegistry;
import tech.thatgravyboat.ironchests.common.utils.ModPaths;
import tech.thatgravyboat.ironchests.common.utils.ModUtils;

import java.io.File;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

public class ChestTypeLoader {

    private static final Gson GSON = new Gson();

    public static void setupChest() {
        File file = new File(ModPaths.LOCK_FILE.toString(), "defaults.lock");
        if (!file.exists()) {
            writeEmptyFile(file);
            setupDefaultFiles("/data/ironchests/chests", ModPaths.CHESTS);
            setupDefaultFiles("/data/ironchests/upgrade_types", ModPaths.UPGRADE_TYPES);
        }
        FileUtils.streamFilesAndParse(ModPaths.CHESTS, ChestTypeLoader::parseChest);
        FileUtils.streamFilesAndParse(ModPaths.UPGRADE_TYPES, ChestTypeLoader::parseChestUpgrade);
    }

    private static void parseChest(Reader reader, String name) {
        JsonObject jsonObject = GsonHelper.fromJson(GSON, reader, JsonObject.class);
        ChestType.codec(name).parse(JsonOps.INSTANCE, jsonObject)
                .result()
                .ifPresent(ChestTypeRegistry.INSTANCE::register);
    }

    private static void parseChestUpgrade(Reader reader, String name) {
        JsonObject jsonObject = GsonHelper.fromJson(GSON, reader, JsonObject.class);
        ChestUpgradeType.CODEC.parse(JsonOps.INSTANCE, jsonObject)
                .result()
                .ifPresent(ChestUpgradeTypeRegistry.INSTANCE::register);
    }

    private static void setupDefaultFiles(String dataPath, Path targetPath) {
        List<Path> roots = ModUtils.getModFilePath(IronChests.MODID);

        if (roots.isEmpty()) {
            throw new RuntimeException("Failed to load defaults.");
        }

        for (Path modRoot : ModUtils.getModFilePath(IronChests.MODID)) {
            FileUtils.copyDefaultFiles(dataPath, targetPath, modRoot);
        }
    }

    private static void writeEmptyFile(File file) {
        try {
            org.apache.commons.io.FileUtils.write(file, "", StandardCharsets.UTF_8);
        } catch (Exception e) {
            //Ignore
        }
    }
}
