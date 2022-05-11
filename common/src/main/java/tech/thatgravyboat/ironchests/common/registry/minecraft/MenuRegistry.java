package tech.thatgravyboat.ironchests.common.registry.minecraft;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestMenu;
import tech.thatgravyboat.ironchests.common.registry.custom.ChestTypeRegistry;

import java.util.function.Supplier;

public class MenuRegistry {

    public static void init() {
        ChestTypeRegistry.INSTANCE.getChests().forEach(MenuRegistry::registerMenu);
    }

    private static void registerMenu(String id, ChestType type) {
        type.registries().setMenuSupplier(registerMenu(id, (syncId, inv) -> new GenericChestMenu(syncId, inv, type)));
    }

    @ExpectPlatform
    public static <E extends AbstractContainerMenu> Supplier<MenuType<E>> registerMenu(String id, MenuFactory<E> item) {
        throw new AssertionError();
    }

    @FunctionalInterface
    public interface MenuFactory<T extends AbstractContainerMenu> {
        T create(int syncId, Inventory inventory);
    }

}
