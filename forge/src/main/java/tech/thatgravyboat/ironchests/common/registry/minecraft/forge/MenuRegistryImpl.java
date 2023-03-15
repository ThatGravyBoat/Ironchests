package tech.thatgravyboat.ironchests.common.registry.minecraft.forge;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import tech.thatgravyboat.ironchests.common.registry.minecraft.MenuRegistry;

import java.util.function.Supplier;

public class MenuRegistryImpl {
    public static <E extends AbstractContainerMenu> Supplier<MenuType<E>> createMenu(String id, MenuRegistry.MenuFactory<E> item) {
        return () -> IForgeMenuType.create((syncId, inv, c) -> item.create(syncId, inv));
    }
}
