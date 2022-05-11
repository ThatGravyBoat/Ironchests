package tech.thatgravyboat.ironchests.common.registry.minecraft.fabric;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.common.registry.minecraft.MenuRegistry;

import java.util.function.Supplier;

public class MenuRegistryImpl {
    public static <E extends AbstractContainerMenu> Supplier<MenuType<E>> registerMenu(String id, MenuRegistry.MenuFactory<E> item) {
        MenuType<E> eMenuType = ScreenHandlerRegistry.registerSimple(new ResourceLocation(IronChests.MODID, id), item::create);
        return () -> eMenuType;
    }
}
