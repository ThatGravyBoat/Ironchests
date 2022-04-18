package tech.thatgravyboat.ironchests.common.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import tech.thatgravyboat.ironchests.common.chesttypes.ChestType;
import tech.thatgravyboat.ironchests.common.chesttypes.ChestTypes;
import tech.thatgravyboat.ironchests.common.chesttypes.ChestUpgradeType;
import tech.thatgravyboat.ironchests.common.chesttypes.ChestUpgradeTypes;
import tech.thatgravyboat.ironchests.common.items.DollyItem;
import tech.thatgravyboat.ironchests.common.items.KeyItem;
import tech.thatgravyboat.ironchests.common.items.LockItem;
import tech.thatgravyboat.ironchests.common.items.UpgradeItem;
import tech.thatgravyboat.ironchests.platform.Services;

import java.util.function.Supplier;

public class ItemRegistry {

    public static void init() {
        ChestTypes.TYPES.values().forEach(ItemRegistry::registerChest);
        ChestUpgradeTypes.UPGRADES.forEach(ItemRegistry::registerUpgrade);
    }

    public static final Supplier<Item> BLANK_UPGRADE = register("blank_chest_upgrade", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final Supplier<Item> LOCK = register("lock", () -> new LockItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final Supplier<Item> KEY = register("key", () -> new KeyItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final Supplier<Item> DIAMOND_DOLLY = register("diamond_dolly", () -> new DollyItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1)));
    public static final Supplier<Item> IRON_DOLLY = register("iron_dolly", () -> new DollyItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC).durability(15)));

    private static Supplier<Item> register(String id, Supplier<Item> item) {
        return Services.REGISTRY.registerItem(id, item);
    }

    private static void registerChest(ChestType type) {
        register(type.name()+"_chest", () -> new BlockItem(type.registries().getBlock().get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    }

    private static void registerUpgrade(ChestUpgradeType type) {
        register(type.to().name()+"_chest_upgrade", () -> new UpgradeItem(type, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    }

}
