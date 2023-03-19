package tech.thatgravyboat.ironchests.fabric;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.common.registry.custom.ChestUpgradeTypeRegistry;
import tech.thatgravyboat.ironchests.common.registry.minecraft.ItemRegistry;

public class IronChestsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        IronChests.init();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((entries) -> {
            entries.accept(ItemRegistry.IRON_DOLLY.get());
            entries.accept(ItemRegistry.DIAMOND_DOLLY.get());
            entries.accept(ItemRegistry.BLANK_UPGRADE.get());
            entries.accept(ItemRegistry.KEY.get());
            entries.accept(ItemRegistry.KEY_RING.get());
            entries.accept(ItemRegistry.LOCK.get());
            ItemRegistry.UPGRADES.boundStream().forEach(entries::accept);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register((entries) -> {
            ItemRegistry.CHESTS.boundStream().forEach(entries::accept);
        });
    }
}
