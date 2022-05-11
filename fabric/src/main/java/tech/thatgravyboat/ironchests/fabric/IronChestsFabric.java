package tech.thatgravyboat.ironchests.fabric;


import net.fabricmc.api.ModInitializer;
import tech.thatgravyboat.ironchests.IronChests;

public class IronChestsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        IronChests.init();
    }
}
