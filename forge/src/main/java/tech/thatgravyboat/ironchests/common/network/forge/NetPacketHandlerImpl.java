package tech.thatgravyboat.ironchests.common.network.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.common.network.handlers.IPacket;
import tech.thatgravyboat.ironchests.common.network.handlers.IPacketHandler;

public class NetPacketHandlerImpl {

    private static int id = 0;
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(IronChests.MODID, "main_channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );


    public static <T extends IPacket<T>> void sendToAllLoaded(T packet, Level level, BlockPos pos) {
        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(pos)), packet);
    }

    public static <T> void registerServerToClientPacket(ResourceLocation location, IPacketHandler<T> handler, Class<T> tClass) {
        clientOnlyRegister(handler, tClass);
    }

    private static <T> void clientOnlyRegister(IPacketHandler<T> handler, Class<T> tClass) {
        INSTANCE.registerMessage(++id, tClass, handler::encode, handler::decode, (t, context) -> {
            Player sender = context.get().getDirection().getReceptionSide().equals(LogicalSide.CLIENT) ? getPlayer() : null;
            if(sender != null) {
                context.get().enqueueWork(() -> handler.handle(t).accept(sender));
            }
            context.get().setPacketHandled(true);
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static Player getPlayer() {
        return Minecraft.getInstance().player;
    }
}
