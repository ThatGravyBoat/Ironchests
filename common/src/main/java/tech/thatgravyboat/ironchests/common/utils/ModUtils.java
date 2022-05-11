package tech.thatgravyboat.ironchests.common.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class ModUtils {
    @ExpectPlatform
    public static Path getConfigDirectory() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static List<Path> getModFilePath(String modid) {
        throw new AssertionError();
    }

    public static Optional<CompoundTag> getTag(ItemStack stack) {
        return Optional.ofNullable(stack.getTag());
    }
}
