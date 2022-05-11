package tech.thatgravyboat.ironchests.api;

import tech.thatgravyboat.ironchests.api.chesttype.ChestUpgradeType;

import java.util.Set;

public interface IUpgradeRegistry {

    Set<ChestUpgradeType> getUpgrades();

    void register(ChestUpgradeType type);
}
