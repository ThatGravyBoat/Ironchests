package tech.thatgravyboat.ironchests.common.network.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;

public interface IPacketHandler<T> {

    void encode(T message, FriendlyByteBuf buffer);

    T decode(FriendlyByteBuf buffer);

    BiConsumer<Minecraft, Player> handle(T message);
}
