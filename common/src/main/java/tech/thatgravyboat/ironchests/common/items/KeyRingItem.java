package tech.thatgravyboat.ironchests.common.items;

import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlockEntity;
import tech.thatgravyboat.ironchests.common.utils.ModUtils;

import java.util.List;

public class KeyRingItem extends KeyItem {

    public KeyRingItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasChest(ItemStack stack) {
        return stack.hasTag() && getKeys(stack).size() > 0;
    }

    @Override
    public boolean canAddNewChest(ItemStack stack) {
        return !stack.hasTag() || getKeys(stack).size() < 8;
    }

    @Override
    public boolean canUseOn(Player player, ItemStack stack, GenericChestBlockEntity chest) {
        if (hasChest(stack)) {
            for (String uuid : getKeys(stack).getAllKeys()) {
                try {
                    if (chest.isRightKey(UUIDTypeAdapter.fromString(uuid))) {
                        return true;
                    }
                } catch (IllegalArgumentException ignored) {}
            }
        }
        return false;
    }

    @Override
    public void addKey(ItemStack stack, GenericChestBlockEntity chest) {
        if (chest.getKeyId() != null) {
            CompoundTag keys = getKeys(stack);
            CompoundTag key = new CompoundTag();
            key.put("chest", NbtUtils.writeBlockPos(chest.getBlockPos()));
            key.putString("chestType", Component.Serializer.toJson(chest.getDisplayName()));
            keys.put(UUIDTypeAdapter.fromUUID(chest.getKeyId()), key);
            var orCreateTag = stack.getOrCreateTag();
            orCreateTag.put("keys", keys);
            stack.setTag(orCreateTag);
        }
    }

    private CompoundTag getKeys(ItemStack stack) {
        return stack.getOrCreateTag().getCompound("keys");
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        list.add(Component.translatable("item.key.keyring.desc"));
        ModUtils.getTag(stack).ifPresent(ctag -> {
            ctag.getCompound("keys").getAllKeys().forEach(uuid -> {
                CompoundTag tag = ctag.getCompound("keys").getCompound(uuid);
                if (tag.contains("chest") && tag.contains("chestType")){
                    BlockPos pos = NbtUtils.readBlockPos(tag.getCompound("chest"));
                    MutableComponent chestType = Component.Serializer.fromJson(tag.getString("chestType"));
                    if (chestType != null) {
                        list.add(Component.translatable("item.key.keychest", chestType, pos.getX(), pos.getY(), pos.getZ()));
                    }
                }
            });
        });
        super.appendHoverText(stack, level, list, tooltipFlag);
    }
}
