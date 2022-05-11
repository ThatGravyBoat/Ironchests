package tech.thatgravyboat.ironchests.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.common.blocks.ISyncableData;
import tech.thatgravyboat.ironchests.common.network.handlers.IPacket;
import tech.thatgravyboat.ironchests.common.network.handlers.IPacketHandler;

import java.util.function.Consumer;

public record SyncMessage(BlockPos pos, CompoundTag tag) implements IPacket<SyncMessage> {

    public static Handler HANDLER = new Handler();
    public static final ResourceLocation ID = new ResourceLocation(IronChests.MODID, "sync");

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public IPacketHandler<SyncMessage> getHandler() {
        return HANDLER;
    }

    private static class Handler implements IPacketHandler<SyncMessage> {

        @Override
        public void encode(SyncMessage message, FriendlyByteBuf buffer) {
            buffer.writeBlockPos(message.pos);
            buffer.writeNbt(message.tag);
        }

        @Override
        public SyncMessage decode(FriendlyByteBuf buffer) {
            return new SyncMessage(buffer.readBlockPos(), buffer.readNbt());
        }

        @Override
        public Consumer<Player> handle(SyncMessage message) {
            return (player) -> {
                if (player != null && player.level.isLoaded(message.pos)) {
                    if (player.level.getBlockEntity(message.pos) instanceof ISyncableData syncableData) {
                        syncableData.loadSyncTag(message.tag);
                    }
                }
            };
        }
    }
}
