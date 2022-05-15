package tech.thatgravyboat.ironchests.common.utils.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModUtilsImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static List<Path> getModFilePath(String modid) {
        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(modid);
        if (container.isEmpty()) return new ArrayList<>();
        return container.get().getOrigin().getPaths();
    }
}
