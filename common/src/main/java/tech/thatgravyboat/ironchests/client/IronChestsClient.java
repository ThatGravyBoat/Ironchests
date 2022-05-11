package tech.thatgravyboat.ironchests.client;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.resources.model.BakedModel;
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
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;
import tech.thatgravyboat.ironchests.common.items.DollyItem;
import tech.thatgravyboat.ironchests.common.registry.custom.ChestTypeRegistry;
import tech.thatgravyboat.ironchests.common.registry.minecraft.ItemRegistry;

public class IronChestsClient {

    public static void init() {
        for (ChestType value : ChestTypeRegistry.INSTANCE.getChests().values()) {
            registerScreen(value.registries().getMenu().get(), ChestScreen::new);
            registerEntityRenderer(value.registries().getBlockEntity().get(), context -> new ChestRenderer<>(value));
            if (value.transparent()) setRenderLayer(value.registries().getBlock().get(), RenderType.translucent());
        }

        registerItemProperty(ItemRegistry.DIAMOND_DOLLY.get(), new ResourceLocation(IronChests.MODID, "dolly_filled"),
                ((itemStack, clientLevel, livingEntity, i) -> DollyItem.getChestId(itemStack)));
        registerItemProperty(ItemRegistry.IRON_DOLLY.get(), new ResourceLocation(IronChests.MODID, "dolly_filled"),
                ((itemStack, clientLevel, livingEntity, i) -> DollyItem.getChestId(itemStack)));
    }

    @ExpectPlatform
    public static void setRenderLayer(Block block, RenderType type) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerItemProperty(Item pItem, ResourceLocation pName, ClampedItemPropertyFunction pProperty) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static BakedModel loadModel(BlockRenderDispatcher dispatcher, ResourceLocation path) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreen(MenuType<H> type, Factory<H, S> factory) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends BlockEntity> void registerEntityRenderer(BlockEntityType<T> type, BlockEntityRendererProvider<T> provider) {
        throw new AssertionError();
    }

    @FunctionalInterface
    public interface Factory<H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> {
        @NotNull S create(H handler, Inventory inventory, Component title);
    }
}
