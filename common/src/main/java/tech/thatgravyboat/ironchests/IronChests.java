package tech.thatgravyboat.ironchests;

import tech.thatgravyboat.ironchests.common.chesttypes.ChestTypeLoader;
import tech.thatgravyboat.ironchests.common.network.NetPacketHandler;
import tech.thatgravyboat.ironchests.common.registry.minecraft.BlockRegistry;
import tech.thatgravyboat.ironchests.common.registry.minecraft.ItemRegistry;
import tech.thatgravyboat.ironchests.common.registry.minecraft.MenuRegistry;

public class IronChests {

    public static final String MODID = "ironchests";
    
    public static void init() {
        ChestTypeLoader.setupChest();

        NetPacketHandler.init();
        BlockRegistry.init();
        ItemRegistry.init();
        MenuRegistry.init();
    }
}
