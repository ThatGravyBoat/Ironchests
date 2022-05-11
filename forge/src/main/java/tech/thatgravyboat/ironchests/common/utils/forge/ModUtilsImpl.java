package tech.thatgravyboat.ironchests.common.utils.forge;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.List;

public class ModUtilsImpl {
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static List<Path> getModFilePath(String modid) {
        return List.of(ModList.get().getModFileById(modid).getFile().getFilePath());
    }
}
