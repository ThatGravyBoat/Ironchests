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

import java.io.Reader;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class ChestTypeLoader {

    private static final Gson GSON = new Gson();

    public static void setupChest() {
        LoaderConfig.load();
        setupDefaultFiles("/data/ironchests/chests", ModPaths.CHESTS, "chests");
        setupDefaultFiles("/data/ironchests/barrels", ModPaths.BARRELS, "barrels");
        setupDefaultFiles("/data/ironchests/upgrade_types", ModPaths.UPGRADE_TYPES, "chest_upgrades");
        FileUtils.streamFilesAndParse(ModPaths.CHESTS, ChestTypeLoader::parseChest, FileUtils::isJson);
        FileUtils.streamFilesAndParse(ModPaths.UPGRADE_TYPES, ChestTypeLoader::parseChestUpgrade, FileUtils::isJson);
        runChestChecks();
        LoaderConfig.save();
    }

    private static void runChestChecks() {
        // Checks if oxidized chest exists.
        ChestTypeRegistry.INSTANCE.getChests()
                .values()
                .stream()
                .map(ChestType::oxidizedChest)
                .filter(Objects::nonNull)
                .forEach(ChestUpgradeType::get);
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

    private static void setupDefaultFiles(String dataPath, Path targetPath, String id) {
        if (LoaderConfig.get(id)) return;
        LoaderConfig.set(id, true);
        setupDefaultFiles(dataPath, targetPath);
    }

    private static void setupDefaultFiles(String dataPath, Path targetPath) {
        List<Path> roots = ModUtils.getModFilePath(IronChests.MODID);

        if (roots.isEmpty()) {
            throw new RuntimeException("Failed to load defaults.");
        }

        for (Path modRoot : roots) {
            FileUtils.copyDefaultFiles(dataPath, targetPath, modRoot, FileUtils::isJson);
        }
    }
}
