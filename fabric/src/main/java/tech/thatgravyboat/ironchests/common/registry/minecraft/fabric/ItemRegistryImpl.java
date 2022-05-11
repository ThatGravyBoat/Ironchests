package tech.thatgravyboat.ironchests.common.registry.minecraft.fabric;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import tech.thatgravyboat.ironchests.IronChests;

import java.util.function.Supplier;

public class ItemRegistryImpl {

    public static <T extends Item> Supplier<T> register(String id, Supplier<T> item) {
        var register = Registry.register(Registry.ITEM, new ResourceLocation(IronChests.MODID, id), item.get());
        return () -> register;
    }
}
