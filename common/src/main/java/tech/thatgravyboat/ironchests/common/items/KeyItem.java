package tech.thatgravyboat.ironchests.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlock;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlockEntity;
import tech.thatgravyboat.ironchests.common.blocks.LockState;
import tech.thatgravyboat.ironchests.common.utils.ModUtils;

import java.util.List;

public class KeyItem extends Item implements UnlockableItem {

    public KeyItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        ItemStack stack = context.getItemInHand();

        if (state.getBlock() instanceof GenericChestBlock && blockEntity instanceof GenericChestBlockEntity chestBlockEntity) {
            if (chestBlockEntity.viewers() > 0) return InteractionResult.PASS;

            if (state.getValue(GenericChestBlock.LOCK).equals(LockState.NO_LOCK)) return InteractionResult.PASS;

            if (canUseOn(context.getPlayer(), stack, chestBlockEntity)) {
                level.setBlock(pos, state.setValue(GenericChestBlock.LOCK, state.getValue(GenericChestBlock.LOCK).opposite()), 2);
                level.updateNeighbourForOutputSignal(pos, state.getBlock());
                return InteractionResult.SUCCESS;
            }

            if ((canAddNewChest(stack)) && state.getValue(GenericChestBlock.LOCK).equals(LockState.UNLOCKED)) {
                addKey(stack, chestBlockEntity);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) {
        return hasChest(itemStack) || super.isFoil(itemStack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        ModUtils.getTag(stack).ifPresent(tag -> {
            if (tag.contains("chest") && tag.contains("chestType")){
                BlockPos pos = NbtUtils.readBlockPos(tag.getCompound("chest"));
                MutableComponent chestType = Component.Serializer.fromJson(tag.getString("chestType"));
                if (chestType != null) list.add(Component.translatable("item.key.chesttype").append(chestType));
                list.add(Component.translatable("item.key.chestpos", pos.getX(), pos.getY(), pos.getZ()));
            }
        });
        super.appendHoverText(stack, level, list, tooltipFlag);
    }
}
