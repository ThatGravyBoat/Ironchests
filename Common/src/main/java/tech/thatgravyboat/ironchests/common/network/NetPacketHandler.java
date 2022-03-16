package tech.thatgravyboat.ironchests.common.network;

import tech.thatgravyboat.ironchests.platform.Services;

public class NetPacketHandler {

    public static void init() {
        Services.NETWORK.registerServerToClientPacket(SyncMessage.ID, SyncMessage.HANDLER, SyncMessage.class);
    }

}
