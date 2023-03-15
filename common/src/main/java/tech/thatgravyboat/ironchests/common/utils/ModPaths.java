package tech.thatgravyboat.ironchests.common.utils;

import tech.thatgravyboat.ironchests.IronChests;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModPaths {

    public static final Path LOCK_FILE = createCustomPath("");
    public static final Path CHESTS = createCustomPath("chests");
    public static final Path BARRELS = createCustomPath("chests/barrels");
    public static final Path UPGRADE_TYPES = createCustomPath("upgrade_types");


    private static Path createCustomPath(String pathName) {
        Path customPath = Paths.get(ModUtils.getConfigDirectory().toAbsolutePath().toString(), IronChests.MODID, pathName);
        createDirectory(customPath, pathName);
        return customPath;
    }

    private static void createDirectory(Path path, String dirName) {
        try {
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException ignored) { //ignored
        } catch (IOException e) {
            System.out.println("failed to create \""+dirName+"\" directory");
        }
    }
}
