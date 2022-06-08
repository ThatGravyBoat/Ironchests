package tech.thatgravyboat.ironchests.api;

import tech.thatgravyboat.ironchests.api.chesttype.ChestType;

import java.util.Map;

public interface IChestRegistry {

    Map<String, ChestType> getChests();

    void register(ChestType type);
}
