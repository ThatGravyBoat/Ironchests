package tech.thatgravyboat.ironchests.common.blocks;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum LockState implements StringRepresentable {
    LOCKED,
    NO_LOCK,
    UNLOCKED;

    public boolean canOpen() {
        return this.equals(NO_LOCK) || this.equals(UNLOCKED);
    }

    public LockState opposite() {
        if (this == NO_LOCK) return NO_LOCK;
        return this == UNLOCKED ? LOCKED : UNLOCKED;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
