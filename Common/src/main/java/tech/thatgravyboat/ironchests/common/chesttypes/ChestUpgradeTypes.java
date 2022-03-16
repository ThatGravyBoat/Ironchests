package tech.thatgravyboat.ironchests.common.chesttypes;

public class ChestUpgradeTypes {
    public static final ChestUpgradeType WOOD_TO_COPPER = new ChestUpgradeType(null, ChestTypes.COPPER);
    public static final ChestUpgradeType COPPER_TO_IRON = new ChestUpgradeType(ChestTypes.COPPER, ChestTypes.IRON);
    public static final ChestUpgradeType IRON_TO_GOLD = new ChestUpgradeType(ChestTypes.IRON, ChestTypes.GOLD);
    public static final ChestUpgradeType GOLD_TO_DIAMOND = new ChestUpgradeType(ChestTypes.GOLD, ChestTypes.DIAMOND);
    public static final ChestUpgradeType DIAMOND_TO_OBSIDIAN = new ChestUpgradeType(ChestTypes.DIAMOND, ChestTypes.OBSIDIAN);
    public static final ChestUpgradeType DIAMOND_TO_CRYSTAL = new ChestUpgradeType(ChestTypes.DIAMOND, ChestTypes.CRYSTAL);
    public static final ChestUpgradeType DIAMOND_TO_NETHERITE = new ChestUpgradeType(ChestTypes.DIAMOND, ChestTypes.NETHERITE);
}
