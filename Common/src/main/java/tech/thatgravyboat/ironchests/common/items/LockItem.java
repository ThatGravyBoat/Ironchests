package tech.thatgravyboat.ironchests.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlock;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlockEntity;
import tech.thatgravyboat.ironchests.common.blocks.LockState;

import java.util.UUID;

public class LockItem extends Item {

    public LockItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (context.getPlayer() == null) return InteractionResult.PASS;

        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (state.getBlock() instanceof GenericChestBlock && blockEntity instanceof GenericChestBlockEntity chestBlockEntity) {
            if (chestBlockEntity.viewers() > 0) return InteractionResult.PASS;
            if (!state.getValue(GenericChestBlock.LOCK).equals(LockState.NO_LOCK)) return InteractionResult.PASS;

            chestBlockEntity.setLockId(UUID.randomUUID());
            level.setBlock(pos, state.setValue(GenericChestBlock.LOCK, LockState.UNLOCKED), 2);
            level.updateNeighbourForOutputSignal(pos, state.getBlock());
            context.getItemInHand().shrink(1);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
