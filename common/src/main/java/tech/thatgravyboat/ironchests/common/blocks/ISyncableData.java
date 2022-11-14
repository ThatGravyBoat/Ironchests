package tech.thatgravyboat.ironchests.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.ironchests.common.network.NetPacketHandler;
import tech.thatgravyboat.ironchests.common.network.SyncMessage;

public interface ISyncableData {

    default void sync(@NotNull Level level, @NotNull BlockPos pos) {
        if (level.isClientSide) return;
        NetPacketHandler.CHANNEL.sendToAllLoaded(new SyncMessage(pos, getSyncTag()), level, pos);
    }

    void loadSyncTag(CompoundTag tag);
    CompoundTag getSyncTag();
}
