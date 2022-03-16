package tech.thatgravyboat.ironchests.platform.services;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

public interface IClientHelper {

    void registerItemProperty(Item pItem, ResourceLocation pName, ClampedItemPropertyFunction pProperty);

    <T extends BlockEntity> void registerEntityRenderer(BlockEntityType<T> type, BlockEntityRendererProvider<T> provider);

    void setRenderLayer(Block block, RenderType type);

    <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreen(MenuType<H> type, Factory<H, S> factory);

    @FunctionalInterface
    public interface Factory<H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> {
        @NotNull S create(H handler, Inventory inventory, Component title);
    }
}
