package tech.thatgravyboat.ironchests.api;

import net.minecraft.resources.ResourceLocation;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;

import java.util.Map;

public interface IChestRegistry {

    Map<String, ChestType> getChests();

    void register(ChestType type);
}
