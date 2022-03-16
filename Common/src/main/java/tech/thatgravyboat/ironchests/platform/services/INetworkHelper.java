package tech.thatgravyboat.ironchests.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import tech.thatgravyboat.ironchests.common.network.handlers.IPacket;
import tech.thatgravyboat.ironchests.common.network.handlers.IPacketHandler;

public interface INetworkHelper {

    <T extends IPacket<T>> void sendToAllLoaded(T packet, Level level, BlockPos pos);

    <T> void registerServerToClientPacket(ResourceLocation location, IPacketHandler<T> handler, Class<T> tClass);
}
