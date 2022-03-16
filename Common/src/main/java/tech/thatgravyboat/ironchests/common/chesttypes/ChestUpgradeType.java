package tech.thatgravyboat.ironchests.common.chesttypes;

public record ChestUpgradeType(ChestType from, ChestType to) {
    public ChestUpgradeType {
        if (from != null && to != null && from.name().equals(to.name())){
            throw new IllegalArgumentException("You can not have a chest upgrade with the same chest type for both to and from.");
        }
    }
}
