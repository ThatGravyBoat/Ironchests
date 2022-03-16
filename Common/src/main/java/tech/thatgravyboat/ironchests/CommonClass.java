package tech.thatgravyboat.ironchests;

import tech.thatgravyboat.ironchests.common.network.NetPacketHandler;
import tech.thatgravyboat.ironchests.common.registry.BlockRegistry;
import tech.thatgravyboat.ironchests.common.registry.ItemRegistry;
import tech.thatgravyboat.ironchests.common.registry.MenuRegistry;

public class CommonClass {

    public static void init() {
        NetPacketHandler.init();
        BlockRegistry.init();
        ItemRegistry.init();
        MenuRegistry.init();
    }
}