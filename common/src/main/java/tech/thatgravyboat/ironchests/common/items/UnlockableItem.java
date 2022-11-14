package tech.thatgravyboat.ironchests.common.items;

import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlockEntity;

public interface UnlockableItem {

    default boolean hasChest(ItemStack stack) {
        return stack.hasTag() && stack.getOrCreateTag().contains("key");
    }

    default boolean canAddNewChest(ItemStack stack) {
        return !stack.hasTag() || !stack.getOrCreateTag().contains("key");
    }

    default boolean canUseOn(Player player, ItemStack stack, GenericChestBlockEntity chest) {
        if (stack.hasTag() && stack.getOrCreateTag().contains("key")) {
            return chest.isRightKey(stack.getOrCreateTag().getUUID("key"));
        }
        return false;
    }

    default void addKey(ItemStack stack, GenericChestBlockEntity chest) {
        if (chest.getKeyId() != null) {
            stack.getOrCreateTag().putUUID("key", chest.getKeyId());
        }
        stack.getOrCreateTag().put("chest", NbtUtils.writeBlockPos(chest.getBlockPos()));
        stack.getOrCreateTag().putString("chestType", Component.Serializer.toJson(chest.getDisplayName()));
    }
}
