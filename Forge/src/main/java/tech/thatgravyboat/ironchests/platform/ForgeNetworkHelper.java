package tech.thatgravyboat.ironchests.platform;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import tech.thatgravyboat.ironchests.Constants;
import tech.thatgravyboat.ironchests.common.network.handlers.IPacket;
import tech.thatgravyboat.ironchests.common.network.handlers.IPacketHandler;
import tech.thatgravyboat.ironchests.platform.services.INetworkHelper;

public class ForgeNetworkHelper implements INetworkHelper {

    private static int id = 0;
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Constants.MODID, "main_channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    @Override
    public <T extends IPacket<T>> void sendToAllLoaded(T packet, Level level, BlockPos pos) {
        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(pos)), packet);
    }

    @Override
    public <T> void registerServerToClientPacket(ResourceLocation location, IPacketHandler<T> handler, Class<T> tClass) {
        clientOnlyRegister(handler, tClass);
    }

    @OnlyIn(Dist.CLIENT)
    private static <T> void clientOnlyRegister(IPacketHandler<T> handler, Class<T> tClass) {
        INSTANCE.registerMessage(++id, tClass, handler::encode, handler::decode, (t, context) -> {
            Player sender = Minecraft.getInstance().player;
            if(sender != null) {
                context.get().enqueueWork(() -> handler.handle(t).accept(Minecraft.getInstance(), sender));
            }
            context.get().setPacketHandled(true);
        });
    }
}
