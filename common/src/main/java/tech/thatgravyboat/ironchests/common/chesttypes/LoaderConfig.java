package tech.thatgravyboat.ironchests.common.chesttypes;

import org.apache.commons.io.FileUtils;
import tech.thatgravyboat.ironchests.common.utils.ModPaths;

import java.io.File;
import java.util.Properties;

public class LoaderConfig {

    private static final File FILE = new File(ModPaths.LOCK_FILE.toString(), "defaults.properties");
    private static final File FILE_OLD = new File(ModPaths.LOCK_FILE.toString(), "defaults.lock");
    private static final Properties PROPERTIES = new Properties();

    public static void load() {
        if (FILE.exists()) {
            try {
                PROPERTIES.load(FileUtils.openInputStream(FILE));
            }catch (Exception e) {
                e.printStackTrace();
            }
        } else if (FILE_OLD.exists()) {
            PROPERTIES.setProperty("chests", "true");
            PROPERTIES.setProperty("chest_upgrades", "true");
            save();
            FILE_OLD.delete();
        }
    }

    public static void save() {
        try {
            PROPERTIES.store(FileUtils.openOutputStream(FILE), "Iron Chests Default Files\nSetting a value will recopy the default files.");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean get(String key) {
        return Boolean.parseBoolean(PROPERTIES.getProperty(key, "false"));
    }

    public static void set(String key, boolean value) {
        PROPERTIES.setProperty(key, String.valueOf(value));
    }
}
