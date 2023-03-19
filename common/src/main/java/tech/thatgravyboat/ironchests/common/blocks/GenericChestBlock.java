package tech.thatgravyboat.ironchests.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.ironchests.api.chesttype.ChestBlockType;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;
import tech.thatgravyboat.ironchests.common.items.UnlockableItem;
import tech.thatgravyboat.ironchests.common.items.UpgradeItem;
import tech.thatgravyboat.ironchests.common.registry.minecraft.ItemRegistry;

@SuppressWarnings("deprecation")
public class GenericChestBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<LockState> LOCK = EnumProperty.create("lock", LockState.class);

    protected final ChestType type;

    protected GenericChestBlock(ChestType type, Properties properties) {
        super(properties);
        this.type = type;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(getFacingProperty(type), Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(LOCK, LockState.NO_LOCK)
                .setValue(OPEN, false)
        );
    }

    public static GenericChestBlock create(ChestType type, Properties properties) {
        final ChestType chestType = type;
        //This is a hack because we need to give the chest type to createBlockStateDefinition but because vanilla calling it
        //in the base constructor, we can't pass it in. So we just need to use an anonymous class to get around this.
        return new GenericChestBlock(type, properties) {
            @Override
            protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
                builder.add(getFacingProperty(chestType), WATERLOGGED, LOCK, OPEN);
            }
        };
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        MenuProvider menu = state.getMenuProvider(level, pos);
        if (menu != null && !isBlockedChestByBlock(level,pos) && state.getValue(LOCK).canOpen()){
            player.openMenu(menu);
        }
        if (!state.getValue(LOCK).canOpen()){
            player.displayClientMessage(Component.translatable("container.isLocked", this.getName()), true);
            player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
        }


        return InteractionResult.CONSUME;
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (stack.hasCustomHoverName() && level.getBlockEntity(pos) instanceof GenericChestBlockEntity chestBlockEntity){
            chestBlockEntity.setCustomName(stack.getHoverName());
        }
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean bl) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof GenericChestBlockEntity containerEntity) {
                Containers.dropContents(level, pos, containerEntity);
                if (containerEntity.canDropLock() && !state.getValue(LOCK).equals(LockState.NO_LOCK)) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), ItemRegistry.LOCK.get().getDefaultInstance());
                }
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, bl);
        }
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return type.shape().shape();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new GenericChestBlockEntity(this.type.registries().getMenu().get(), this.type.registries().getBlockEntity().get(),  pos, state, this.type);
    }

    //region BlockState Stuff


    @Override
    public float getDestroyProgress(@NotNull BlockState state, @NotNull Player player, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        if (state.hasProperty(LOCK) && !state.getValue(LOCK).canOpen() && level.getBlockEntity(pos) instanceof GenericChestBlockEntity chest) {
            return player.getInventory().items.stream().filter(stack -> stack.getItem() instanceof UnlockableItem)
                    .anyMatch(item -> ((UnlockableItem) item.getItem()).canUseOn(player, item, chest)) ? super.getDestroyProgress(state, player, level, pos) : 0.001f;
        }
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    public @NotNull BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(getFacingProperty(type))));
    }

    @Override
    public @NotNull BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(getFacingProperty(type), rotation.rotate(blockState.getValue(getFacingProperty(type))));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        Direction direction = context.getHorizontalDirection().getOpposite();
        if (getFacingProperty(type).equals(BlockStateProperties.FACING)) {
            direction = context.getNearestLookingDirection().getOpposite();
        }
        return this.defaultBlockState().setValue(getFacingProperty(type), direction).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER).setValue(LOCK, LockState.NO_LOCK);
    }



    @Override
    public @NotNull FluidState getFluidState(BlockState blockState) {
        return Boolean.TRUE.equals(blockState.getValue(WATERLOGGED)) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, @NotNull Direction direction, @NotNull BlockState blockState2, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockPos blockPos2) {
        if (Boolean.TRUE.equals(blockState.getValue(WATERLOGGED))) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);

    }

    //endregion

    private boolean isBlockedChestByBlock(BlockGetter blockGetter, BlockPos blockPos) {
        if (!this.type.blockType().canBeBlocked()) return false;
        BlockPos blockPos2 = blockPos.above();
        return blockGetter.getBlockState(blockPos2).isRedstoneConductor(blockGetter, blockPos2);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        return type.blockType() == ChestBlockType.CHEST ? RenderShape.ENTITYBLOCK_ANIMATED : RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? createTickerHelper(blockEntityType, this.type.registries().getBlockEntity().get(), GenericChestBlockEntity::lidAnimateTick) : null;
    }

    @Override
    public void randomTick(@NotNull BlockState state, ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (level.random.nextFloat() > 0.05f) return;
        if (level.getBlockEntity(pos) instanceof GenericChestBlockEntity chest) {
            ChestType newChest = chest.getChestType().getOxidizedChest();
            if (newChest != null){
                UpgradeItem.changeToChest(level, pos, chest, newChest);
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return type.oxidizedChest() != null;
    }

    public static Property<Direction> getFacingProperty(ChestType type) {
        return type.blockType() == ChestBlockType.BARREL ? BlockStateProperties.FACING : BlockStateProperties.HORIZONTAL_FACING;
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(@NotNull BlockState state, Level level, @NotNull BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof GenericChestBlockEntity chest){
            return AbstractContainerMenu.getRedstoneSignalFromContainer(chest);
        }
        return 0;
    }
}
