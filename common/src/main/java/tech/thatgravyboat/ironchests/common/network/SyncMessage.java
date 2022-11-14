package tech.thatgravyboat.ironchests.common.network;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.common.blocks.ISyncableData;

public record SyncMessage(BlockPos pos, CompoundTag tag) implements Packet<SyncMessage> {

    public static Handler HANDLER = new Handler();
    public static final ResourceLocation ID = new ResourceLocation(IronChests.MODID, "sync");

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<SyncMessage> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<SyncMessage> {

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
        public PacketContext handle(SyncMessage message) {
            return (player, level) -> {
                if (player != null && level.isLoaded(message.pos)) {
                    if (level.getBlockEntity(message.pos) instanceof ISyncableData syncableData) {
                        syncableData.loadSyncTag(message.tag);
                    }
                }
            };
        }
    }
}
