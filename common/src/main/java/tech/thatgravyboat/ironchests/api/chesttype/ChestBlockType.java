package tech.thatgravyboat.ironchests.api.chesttype;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.Locale;

public enum ChestBlockType {
    CHEST,
    BLOCK,
    BARREL;

    public String id() {
        return name().toLowerCase(Locale.ROOT);
    }

    public boolean canBeBlocked() {
        return this == CHEST;
    }

    public SoundEvent getOpenSound() {
        return this == BARREL ? SoundEvents.BARREL_OPEN : SoundEvents.CHEST_OPEN;
    }

    public SoundEvent getCloseSound() {
        return this == BARREL ? SoundEvents.BARREL_CLOSE : SoundEvents.CHEST_CLOSE;
    }
}
