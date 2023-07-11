package tech.thatgravyboat.ironchests.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;
import tech.thatgravyboat.ironchests.api.chesttype.ChestUpgradeType;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlockEntity;

import java.util.UUID;

public class UpgradeItem extends Item {

    public static final TagKey<Block> REPLACEABLE_CHEST_TAG = TagKey.create(Registries.BLOCK, new ResourceLocation(IronChests.MODID, "upgradeable_wooden_chests"));

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

            if (!chestEntity.canOpen(context.getPlayer())) return InteractionResult.PASS;
            if(!chestEntity.getChestType().equals(type.from())) return InteractionResult.PASS;

            UpgradeItem.changeToChest(level, pos, chestEntity, type.to());
            context.getItemInHand().shrink(1);

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        BlockState state = level.getBlockState(pos);

        if (blockEntity instanceof ChestBlockEntity chestEntity){

            if (ChestBlockEntity.getOpenCount(level, pos) > 0) return InteractionResult.PASS;
            if (!state.is(REPLACEABLE_CHEST_TAG)) return InteractionResult.PASS;
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

    public static void changeToChest(Level level, BlockPos pos, GenericChestBlockEntity chest, ChestType to) {
        if (chest.viewers() > 0) return;
        if (level.isClientSide) return;

        NonNullList<ItemStack> contents = chest.getItems();
        BlockState blockState = to.registries().getBlock().get().withPropertiesOf(level.getBlockState(pos));
        GenericChestBlockEntity chestBlockEntity = to.registries().getBlockEntity().get().create(pos, blockState);
        Component displayName = chest.getCustomName();
        UUID key = chest.getKeyId();

        if (chestBlockEntity == null) return;

        level.removeBlockEntity(pos);
        level.removeBlock(pos, false);

        level.setBlock(pos, blockState, 3);
        level.setBlockEntity(chestBlockEntity);

        if (displayName != null) chestBlockEntity.setCustomName(displayName);
        if (key != null) chestBlockEntity.setLockId(key);
        chestBlockEntity.setItems(contents);
    }
}
