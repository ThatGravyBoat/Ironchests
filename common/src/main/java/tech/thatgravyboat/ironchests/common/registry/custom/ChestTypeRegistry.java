package tech.thatgravyboat.ironchests.common.registry.custom;

import tech.thatgravyboat.ironchests.api.IChestRegistry;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChestTypeRegistry implements IChestRegistry {

    public static final ChestTypeRegistry INSTANCE = new ChestTypeRegistry();

    private final Map<String, ChestType> CHESTS = new HashMap<>();

    private ChestTypeRegistry() {}

    @Override
    public Map<String, ChestType> getChests() {
        return Collections.unmodifiableMap(CHESTS);
    }

    @Override
    public void register(ChestType type) {
        String name = String.format("%s_%s", type.name().toLowerCase(Locale.ROOT), type.blockType().id());
        if (CHESTS.containsKey(name)) {
            throw new IllegalArgumentException("Chest of '" + name + "' already exists.");
        }
        CHESTS.put(name, type);
    }
}
