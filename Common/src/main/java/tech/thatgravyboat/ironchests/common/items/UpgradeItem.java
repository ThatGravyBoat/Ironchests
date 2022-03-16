package tech.thatgravyboat.ironchests.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlockEntity;
import tech.thatgravyboat.ironchests.common.chesttypes.ChestUpgradeType;

public class UpgradeItem extends Item {

    public final ChestUpgradeType type;

    public UpgradeItem(ChestUpgradeType type, Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (context.getPlayer() == null) return InteractionResult.PASS;

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof GenericChestBlockEntity chestEntity){

            if (chestEntity.viewers() > 0) return InteractionResult.PASS;
            if (!chestEntity.getChestType().equals(type.from())) return InteractionResult.PASS;
            if (!chestEntity.canOpen(context.getPlayer())) return InteractionResult.PASS;
            if (level.isClientSide) return InteractionResult.SUCCESS;

            NonNullList<ItemStack> contents = chestEntity.getItems();
            BlockState blockState = type.to().registries().getBlock().get().withPropertiesOf(context.getLevel().getBlockState(pos));
            GenericChestBlockEntity chestBlockEntity = type.to().registries().getBlockEntity().get().create(context.getClickedPos(), blockState);
            Component displayName = chestEntity.getCustomName();

            if (chestBlockEntity == null) return InteractionResult.PASS;

            level.removeBlockEntity(pos);
            level.removeBlock(pos, false);

            level.setBlock(pos, blockState, 3);
            level.setBlockEntity(chestBlockEntity);

            context.getItemInHand().shrink(1);

            if (displayName != null) chestBlockEntity.setCustomName(displayName);
            chestBlockEntity.setItems(contents);

            return InteractionResult.CONSUME;
        }

        if (blockEntity instanceof ChestBlockEntity chestEntity){

            if (ChestBlockEntity.getOpenCount(level, pos) > 0) return InteractionResult.PASS;
            if (type.from() != null) return InteractionResult.PASS;
            if (!chestEntity.canOpen(context.getPlayer())) return InteractionResult.PASS;
            if (level.isClientSide) return InteractionResult.SUCCESS;

            NonNullList<ItemStack> contents = NonNullList.withSize(chestEntity.getContainerSize(), ItemStack.EMPTY);
            for (int i = 0; i < chestEntity.getContainerSize(); i++) contents.set(i, chestEntity.getItem(i));

            BlockState blockState = type.to().registries().getBlock().get().withPropertiesOf(context.getLevel().getBlockState(pos));
            GenericChestBlockEntity chestBlockEntity = type.to().registries().getBlockEntity().get().create(context.getClickedPos(), blockState);
            Component displayName = chestEntity.getCustomName();

            if (chestBlockEntity == null) return InteractionResult.PASS;

            level.removeBlockEntity(pos);
            level.removeBlock(pos, false);

            level.setBlock(pos, blockState, 3);
            level.setBlockEntity(chestBlockEntity);

            context.getItemInHand().shrink(1);

            if (displayName != null) chestBlockEntity.setCustomName(displayName);
            chestBlockEntity.setItems(contents);

            return InteractionResult.CONSUME;
        }






        return InteractionResult.PASS;
    }
}
