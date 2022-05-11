package tech.thatgravyboat.ironchests.common.network;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import tech.thatgravyboat.ironchests.common.network.handlers.IPacket;
import tech.thatgravyboat.ironchests.common.network.handlers.IPacketHandler;

public class NetPacketHandler {

    public static void init() {
        registerServerToClientPacket(SyncMessage.ID, SyncMessage.HANDLER, SyncMessage.class);
    }

    @ExpectPlatform
    public static <T extends IPacket<T>> void sendToAllLoaded(T packet, Level level, BlockPos pos) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T> void registerServerToClientPacket(ResourceLocation location, IPacketHandler<T> handler, Class<T> tClass) {
        throw new AssertionError();
    }

}
