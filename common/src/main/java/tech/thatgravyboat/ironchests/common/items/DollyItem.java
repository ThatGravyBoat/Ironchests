package tech.thatgravyboat.ironchests.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlockEntity;

import java.util.List;

public class DollyItem extends Item {

    public static final TagKey<Block> NONPICKABLE_CHEST_TAG = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(IronChests.MODID, "non_pickable_chests"));

    public DollyItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.PASS;

        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        ItemStack stack = context.getItemInHand();

        if (hasChest(stack) && !player.isShiftKeyDown()) return InteractionResult.PASS;

        if (hasChest(stack) && player.isShiftKeyDown()) {
            BlockPlaceContext blockPlaceContext = new BlockPlaceContext(context);
            if (!blockPlaceContext.canPlace()) return InteractionResult.PASS;
            //noinspection ConstantConditions
            BlockState tagState = updateBlockState(blockPlaceContext, NbtUtils.readBlockState(stack.getTag().getCompound("BlockStateTag")));
            level.setBlock(blockPlaceContext.getClickedPos(), tagState, 11);
            loadBlockEntityTag(level.getBlockEntity(blockPlaceContext.getClickedPos()), stack.getTag().getCompound("BlockEntityTag"));
            stack.getTag().remove("BlockStateTag");
            stack.getTag().remove("BlockEntityTag");
            stack.hurtAndBreak(1, player, player2 -> player2.broadcastBreakEvent(player.getUsedItemHand()));
            return InteractionResult.SUCCESS;
        }

        if (!state.is(NONPICKABLE_CHEST_TAG) && !hasChest(stack) && ((blockEntity instanceof GenericChestBlockEntity chest && chest.viewers() == 0) || (blockEntity instanceof ChestBlockEntity && ChestBlockEntity.getOpenCount(level, pos) == 0))){
            stack.getOrCreateTag().put("BlockStateTag", NbtUtils.writeBlockState(state));
            stack.getOrCreateTag().put("BlockEntityTag", blockEntity.saveWithFullMetadata());
            level.removeBlockEntity(pos);
            //This is a hack because for some reason forge doesn't remove the block entity right away while on fabric it does?
            if (blockEntity instanceof GenericChestBlockEntity chest) {
                chest.setCanDropLock(false);
            }
            level.removeBlock(pos, false);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private static void loadBlockEntityTag(BlockEntity blockEntity, CompoundTag tag){
        if (blockEntity != null) {
            CompoundTag compoundTag2 = blockEntity.saveWithFullMetadata();
            CompoundTag compoundTag3 = compoundTag2.copy();
            compoundTag2.merge(tag);
            compoundTag2.putInt("x", blockEntity.getBlockPos().getX());
            compoundTag2.putInt("y", blockEntity.getBlockPos().getY());
            compoundTag2.putInt("z", blockEntity.getBlockPos().getZ());
            if (!compoundTag2.equals(compoundTag3)) {
                blockEntity.load(compoundTag2);
                blockEntity.setChanged();
            }
        }
    }

    private static BlockState updateBlockState(BlockPlaceContext context, BlockState newState){
        if (newState.hasProperty(BlockStateProperties.WATERLOGGED)) {
            newState = newState.setValue(BlockStateProperties.WATERLOGGED, context.getLevel().getBlockState(context.getClickedPos()).is(Blocks.WATER));
        }
        if (newState.hasProperty(ChestBlock.TYPE)){
            newState = newState.setValue(ChestBlock.TYPE, ChestType.SINGLE);
        }
        if (newState.hasProperty(HorizontalDirectionalBlock.FACING)){
            newState = newState.setValue(HorizontalDirectionalBlock.FACING, context.getHorizontalDirection().getOpposite());
        }
        return newState;
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int i, boolean bl) {
        if (hasChest(itemStack) && entity instanceof Player player && !player.isCreative() && !player.isSpectator()){
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20, 3));
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        if (hasChest(stack)){
            String id = stack.getOrCreateTag().getCompound("BlockStateTag").getString("Name");
            list.add(Component.translatable("item.dolly.chestid", id));
        }
        super.appendHoverText(stack, level, list, tooltipFlag);
    }

    public static boolean hasChest(ItemStack stack){
        return stack.hasTag() && stack.getOrCreateTag().contains("BlockStateTag") && stack.getOrCreateTag().contains("BlockEntityTag");
    }

    public static float getChestId(ItemStack stack){
        return hasChest(stack) ? 1f : -1f;
    }

}
