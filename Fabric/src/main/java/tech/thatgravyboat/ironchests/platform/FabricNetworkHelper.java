package tech.thatgravyboat.ironchests.platform;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.Level;
import tech.thatgravyboat.ironchests.common.network.handlers.IPacket;
import tech.thatgravyboat.ironchests.common.network.handlers.IPacketHandler;
import tech.thatgravyboat.ironchests.platform.services.INetworkHelper;

public class FabricNetworkHelper implements INetworkHelper {

    @Override
    public <T extends IPacket<T>> void sendToAllLoaded(T packet, Level level, BlockPos pos) {
        var chunk = level.getChunkAt(pos);
        ((ServerChunkCache)level.getChunkSource()).chunkMap.getPlayers(chunk.getPos(), false)
                .forEach(player -> {
                    var buf = new FriendlyByteBuf(Unpooled.buffer());
                    packet.getHandler().encode(packet, buf);
                    ServerPlayNetworking.send(player, packet.getID(), buf);
                });
    }

    @Override
    public <T> void registerServerToClientPacket(ResourceLocation location, IPacketHandler<T> handler, Class<T> tClass) {
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT))
            clientOnlyRegister(location, handler);
    }

    @Environment(EnvType.CLIENT)
    private static <T> void clientOnlyRegister(ResourceLocation location, IPacketHandler<T> handler) {
        ClientPlayNetworking.registerGlobalReceiver(location, (client, handler1, buf, responseSender) -> {
            var decode = handler.decode(buf);
            client.execute(() -> handler.handle(decode).accept(client.player));
        });
    }
}
