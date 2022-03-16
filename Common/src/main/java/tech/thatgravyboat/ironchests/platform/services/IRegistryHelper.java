package tech.thatgravyboat.ironchests.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface IRegistryHelper {

    <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item);

    <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> item);

    <E extends BlockEntity, T extends BlockEntityType<E>> Supplier<T> registerBlockEntity(String id, Supplier<T> item);

    <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(BlockEntityFactory<E> factory, Block... blocks);

    <E extends AbstractContainerMenu> Supplier<MenuType<E>> registerMenu(String id, MenuFactory<E> item);

    @FunctionalInterface
    interface BlockEntityFactory<T extends BlockEntity> {
        @NotNull T create(BlockPos blockPos, BlockState blockState);
    }

    @FunctionalInterface
    interface MenuFactory<T extends AbstractContainerMenu> {
        T create(int syncId, Inventory inventory);
    }

}
