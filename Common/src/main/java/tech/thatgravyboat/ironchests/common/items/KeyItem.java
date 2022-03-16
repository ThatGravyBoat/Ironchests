package tech.thatgravyboat.ironchests.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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

import java.util.List;

public class KeyItem extends Item {

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

            if (stack.hasTag() && stack.getTag().contains("key") && chestBlockEntity.isRightKey(stack)){
                level.setBlock(pos, state.setValue(GenericChestBlock.LOCK, state.getValue(GenericChestBlock.LOCK).opposite()), 2);
                level.updateNeighbourForOutputSignal(pos, state.getBlock());
                return InteractionResult.SUCCESS;
            }

            if ((!stack.hasTag() || !stack.getTag().contains("key")) && state.getValue(GenericChestBlock.LOCK).equals(LockState.UNLOCKED)){
                chestBlockEntity.setLockKey(stack);
                stack.getOrCreateTag().put("chest", NbtUtils.writeBlockPos(pos));
                stack.getOrCreateTag().putString("chestType", Component.Serializer.toJson(chestBlockEntity.getDisplayName()));
                return InteractionResult.SUCCESS;
            }

        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) {
        return hasKeyId(itemStack) || super.isFoil(itemStack);
    }

    private boolean hasKeyId(ItemStack stack){
        return stack.hasTag() && stack.getTag().contains("key");
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        if (stack.hasTag() && stack.getTag().contains("chest") && stack.getTag().contains("chestType")){
            BlockPos pos = NbtUtils.readBlockPos(stack.getTag().getCompound("chest"));
            list.add(new TranslatableComponent("item.key.chesttype").append(Component.Serializer.fromJson(stack.getTag().getString("chestType"))));
            list.add(new TranslatableComponent("item.key.chestpos", pos.getX(), pos.getY(), pos.getZ()));
        }

        super.appendHoverText(stack, level, list, tooltipFlag);
    }
}
