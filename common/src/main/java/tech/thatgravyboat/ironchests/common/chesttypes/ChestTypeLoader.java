package tech.thatgravyboat.ironchests.common.chesttypes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.GsonHelper;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;
import tech.thatgravyboat.ironchests.api.chesttype.ChestUpgradeType;
import tech.thatgravyboat.ironchests.common.registry.custom.ChestTypeRegistry;
import tech.thatgravyboat.ironchests.common.registry.custom.ChestUpgradeTypeRegistry;
import tech.thatgravyboat.ironchests.common.utils.FileUtils;
import tech.thatgravyboat.ironchests.common.utils.ModPaths;

import java.io.File;
import java.io.Reader;
import java.util.Objects;

public class ChestTypeLoader {

    private static final Gson GSON = new Gson();

    public static void setupChest() {
        File file = new File(ModPaths.LOCK_FILE.toString(), "defaults.lock");
        if (!file.exists()) {
            FileUtils.writeEmptyFile(file);
            FileUtils.setupDefaultFiles("/data/ironchests/chests", ModPaths.CHESTS);
            FileUtils.setupDefaultFiles("/data/ironchests/upgrade_types", ModPaths.UPGRADE_TYPES);
        }
        FileUtils.streamFilesAndParse(ModPaths.CHESTS, ChestTypeLoader::parseChest, "Could not stream chests!");
        FileUtils.streamFilesAndParse(ModPaths.UPGRADE_TYPES, ChestTypeLoader::parseChestUpgrade, "Could not stream chest upgrades!");
        runChestChecks();
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
}
