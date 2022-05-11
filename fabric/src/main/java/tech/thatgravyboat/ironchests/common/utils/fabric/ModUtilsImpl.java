package tech.thatgravyboat.ironchests.common.utils.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ModUtilsImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static List<Path> getModFilePath(String modid) {
        return FabricLoader.getInstance().getModContainer(modid).map(ModContainer::getRootPaths).orElse(new ArrayList<>());
    }
}
