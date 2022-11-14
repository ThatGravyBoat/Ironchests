package tech.thatgravyboat.ironchests.common.network;

import com.teamresourceful.resourcefullib.common.networking.NetworkChannel;
import com.teamresourceful.resourcefullib.common.networking.base.NetworkDirection;
import tech.thatgravyboat.ironchests.IronChests;

public class NetPacketHandler {

    public static final NetworkChannel CHANNEL = new NetworkChannel(IronChests.MODID, 1, "main");

    public static void init() {
        CHANNEL.registerPacket(NetworkDirection.SERVER_TO_CLIENT, SyncMessage.ID, SyncMessage.HANDLER, SyncMessage.class);
    }

}
