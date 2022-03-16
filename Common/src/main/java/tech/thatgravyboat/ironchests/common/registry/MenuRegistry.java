package tech.thatgravyboat.ironchests.common.registry;

import tech.thatgravyboat.ironchests.common.GenericChestMenu;
import tech.thatgravyboat.ironchests.common.chesttypes.ChestType;
import tech.thatgravyboat.ironchests.common.chesttypes.ChestTypes;
import tech.thatgravyboat.ironchests.platform.Services;

public class MenuRegistry {

    public static void init() {
        ChestTypes.TYPES.values().forEach(MenuRegistry::registerMenu);
    }

    private static void registerMenu(ChestType type) {
        type.registries().setMenuSupplier(Services.REGISTRY.registerMenu(type.name()+"_chest", (id, inv) -> new GenericChestMenu(id, inv, type)));
    }
}
