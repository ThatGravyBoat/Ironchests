package tech.thatgravyboat.ironchests.common.registry.minecraft;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;
import tech.thatgravyboat.ironchests.api.chesttype.ChestUpgradeType;
import tech.thatgravyboat.ironchests.common.items.*;
import tech.thatgravyboat.ironchests.common.registry.custom.ChestTypeRegistry;
import tech.thatgravyboat.ironchests.common.registry.custom.ChestUpgradeTypeRegistry;

import java.util.function.Supplier;

public class ItemRegistry {

    public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, IronChests.MODID);
    public static final ResourcefulRegistry<Item> UPGRADES = ResourcefulRegistries.create(ITEMS);
    public static final ResourcefulRegistry<Item> CHESTS = ResourcefulRegistries.create(ITEMS);

    public static void init() {
        ChestTypeRegistry.INSTANCE.getChests().forEach(ItemRegistry::registerChest);
        ChestUpgradeTypeRegistry.INSTANCE.getUpgrades().forEach(ItemRegistry::registerUpgrade);

        ITEMS.init();
    }

    public static final Supplier<Item> BLANK_UPGRADE = ITEMS.register("blank_chest_upgrade", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> LOCK = ITEMS.register("lock", () -> new LockItem(new Item.Properties()));
    public static final Supplier<Item> KEY = ITEMS.register("key", () -> new KeyItem(new Item.Properties()));
    public static final Supplier<Item> KEY_RING = ITEMS.register("key_ring", () -> new KeyRingItem(new Item.Properties()));
    public static final Supplier<Item> DIAMOND_DOLLY = ITEMS.register("diamond_dolly", () -> new DollyItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> IRON_DOLLY = ITEMS.register("iron_dolly", () -> new DollyItem(new Item.Properties().durability(15)));

    private static void registerChest(String id, ChestType type) {
        CHESTS.register(id, () -> new BlockItem(type.registries().getBlock().get(), getProps(type)));
    }

    private static Item.Properties getProps(ChestType type) {
        Item.Properties properties = new Item.Properties();
        if (type.fireResistant()) {
            properties = properties.fireResistant();
        }
        return properties;
    }

    private static void registerUpgrade(ChestUpgradeType type) {
        UPGRADES.register(type.to().name()+"_chest_upgrade", () -> new UpgradeItem(type, new Item.Properties()));
    }

}
