package tech.thatgravyboat.ironchests.common.registry.minecraft.forge;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tech.thatgravyboat.ironchests.IronChests;

import java.util.function.Supplier;

public class ItemRegistryImpl {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, IronChests.MODID);

    public static <T extends Item> Supplier<T> register(String id, Supplier<T> item) {
        return ITEMS.register(id, item);
    }
}
