package tech.thatgravyboat.ironchests.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;

import java.util.Locale;
import java.util.UUID;
import java.util.stream.IntStream;

public class GenericChestBlockEntity extends BaseContainerBlockEntity implements ISyncableData, WorldlyContainer {

    private final int[] slots;
    private NonNullList<ItemStack> items;
    private final ChestType type;
    private final MenuType<GenericChestMenu> menuType;
    private final ContainerOpenersCounter openersCounter;
    private UUID keyId;
    private float openness;
    private float lastOpenness;

    private boolean dropLock;

    public GenericChestBlockEntity(BlockPos blockPos, BlockState blockState, ChestType type){
        this(type.registries().getMenu().get(), type.registries().getBlockEntity().get(), blockPos, blockState, type);
    }

    public GenericChestBlockEntity(MenuType<GenericChestMenu> menuType, BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, ChestType type) {
        super(blockEntityType, blockPos, blockState);
        this.openersCounter = new  ContainerOpenersCounter() {

            @Override
            protected void onOpen(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
                sync(level, blockPos);
                level.playSound(null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, type.blockType().getOpenSound(), SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
            }

            @Override
            protected void onClose(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
                level.playSound(null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, type.blockType().getCloseSound(), SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
            }

            @Override
            protected void openerCountChanged(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, int i, int j) {
                if (blockState.hasProperty(GenericChestBlock.OPEN)) {
                    level.setBlock(blockPos, blockState.setValue(GenericChestBlock.OPEN, j > 0), Block.UPDATE_ALL);
                }
            }

            @Override
            protected boolean isOwnContainer(@NotNull Player player) {
                return player.containerMenu instanceof ChestMenu menu && menu.getContainer() == GenericChestBlockEntity.this;
            }
        };
        this.items = NonNullList.withSize(type.size(), ItemStack.EMPTY);
        this.type = type;
        this.menuType = menuType;
        this.slots = IntStream.range(0, type.size()).toArray();
    }

    //region Container/Screen

    @Override
    protected @NotNull Component getDefaultName() {
        return type.registries().getBlock().get().getName();
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory) {
        return new GenericChestMenu(menuType, i, inventory, this, type);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (this.level == null || this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    //endregion

    //region Item Stuff

    @Override
    public int getContainerSize() {
        return this.type.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        return items.get(i);
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public @NotNull ItemStack removeItem(int i, int j) {
        ItemStack itemStack = ContainerHelper.removeItem(items, i, j);
        if (!itemStack.isEmpty()) this.setChanged();
        return itemStack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(items, i);
    }

    @Override
    public void setItem(int i, @NotNull ItemStack itemStack) {
        items.set(i, itemStack);
        if (itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    public void setItems(NonNullList<ItemStack> items){
        if (items.size() == this.getContainerSize()) this.items = items;
        else {
            this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            for (int i = 0; i < items.size(); i++) this.items.set(i, items.get(i));
        }
        this.setChanged();
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public boolean canPlaceItem(int i, @NotNull ItemStack itemStack) {
        return this.type.predicate().matches(itemStack);
    }

    //endregion

    //region NBT

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (compoundTag.contains("key")) this.keyId = compoundTag.getUUID("key");
        ContainerHelper.loadAllItems(compoundTag, this.items);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, this.items);
        if (keyId != null) compoundTag.putUUID("key", keyId);
    }

    //endregion

    //region Opening Stuff

    public int viewers() {
        return openersCounter.getOpenerCount();
    }

    @Override
    public void startOpen(@NotNull Player player) {
        if (this.level != null)
            this.openersCounter.incrementOpeners(player, this.level, this.getBlockPos(), this.getBlockState());
    }

    @Override
    public void stopOpen(@NotNull Player player) {
        if (this.level != null)
            this.openersCounter.decrementOpeners(player, this.level, this.getBlockPos(), this.getBlockState());
    }

    public float getOpenness(float partialTicks){
        return Mth.lerp(partialTicks, lastOpenness, openness);
    }

    @SuppressWarnings("unused")
    public static void lidAnimateTick(Level level, BlockPos blockPos, BlockState blockState, GenericChestBlockEntity chestBlockEntity) {
        chestBlockEntity.lastOpenness = chestBlockEntity.openness;
        if (blockState.getValue(GenericChestBlock.OPEN) && chestBlockEntity.openness < 90f) chestBlockEntity.openness = Math.min(chestBlockEntity.openness + 12, 90);
        else if (!blockState.getValue(GenericChestBlock.OPEN) && chestBlockEntity.openness > 0f) chestBlockEntity.openness = Math.max(chestBlockEntity.openness - 12, 0);
    }

    //endregion

    //region Render Item Syncing

    @Override
    public void setChanged() {
        if (type.renderItems() && this.level != null) {
            sync(this.level, this.worldPosition);
        }
        super.setChanged();
    }

    @Override
    public void loadSyncTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public CompoundTag getSyncTag() {
        var tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }
    //endregion

    //region locking

    public void setLockId(UUID uuid){
        this.keyId = uuid;
    }

    public boolean isRightKey(UUID key) {
        return key.equals(keyId);
    }

    public UUID getKeyId() {
        return keyId;
    }

    public boolean canDropLock() {
        return this.dropLock;
    }

    public void setCanDropLock(boolean dropLock) {
        this.dropLock = dropLock;
    }

    //endregion

    public ChestType getChestType() {
        return type;
    }

    //region Hopper Stuff
    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction direction) {
        return slots;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        return keyId == null || this.getBlockState().getValue(GenericChestBlock.LOCK).canOpen();
    }

    @Override
    public boolean canTakeItemThroughFace(int i, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        return keyId == null || this.getBlockState().getValue(GenericChestBlock.LOCK).canOpen();
    }
    //endregion
}
