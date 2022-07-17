package tech.thatgravyboat.ironchests.common.registry.minecraft.forge;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.common.registry.minecraft.MenuRegistry;

import java.util.function.Supplier;

public class MenuRegistryImpl {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, IronChests.MODID);

    public static <E extends AbstractContainerMenu> Supplier<MenuType<E>> registerMenu(String id, MenuRegistry.MenuFactory<E> item) {
        return MENU_TYPES.register(id, () -> IForgeMenuType.create((syncId, inv, c) -> item.create(syncId, inv)));
    }
}
