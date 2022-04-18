package tech.thatgravyboat.ironchests.common.chesttypes;

import java.util.LinkedHashSet;
import java.util.Set;

public class ChestUpgradeTypes {

    public static final Set<ChestUpgradeType> UPGRADES = new LinkedHashSet<>();

    private static final ChestUpgradeType WOOD_TO_COPPER = register(null, ChestTypes.COPPER);
    private static final ChestUpgradeType COPPER_TO_IRON = register(ChestTypes.COPPER, ChestTypes.IRON);
    private static final ChestUpgradeType IRON_TO_GOLD = register(ChestTypes.IRON, ChestTypes.GOLD);
    private static final ChestUpgradeType GOLD_TO_DIAMOND = register(ChestTypes.GOLD, ChestTypes.DIAMOND);
    private static final ChestUpgradeType DIAMOND_TO_OBSIDIAN = register(ChestTypes.DIAMOND, ChestTypes.OBSIDIAN);
    private static final ChestUpgradeType DIAMOND_TO_CRYSTAL = register(ChestTypes.DIAMOND, ChestTypes.CRYSTAL);
    private static final ChestUpgradeType DIAMOND_TO_NETHERITE = register(ChestTypes.DIAMOND, ChestTypes.NETHERITE);

    private static ChestUpgradeType register(ChestType from, ChestType to) {
        var chestUpgradeType = new ChestUpgradeType(from, to);
        UPGRADES.add(chestUpgradeType);
        return chestUpgradeType;
    }
}
