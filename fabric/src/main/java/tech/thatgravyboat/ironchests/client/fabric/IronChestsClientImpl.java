package tech.thatgravyboat.ironchests.client.fabric;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.BakedModelManagerHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import tech.thatgravyboat.ironchests.client.IronChestsClient;

public class IronChestsClientImpl {
    public static void setRenderLayer(Block block, RenderType type) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, type);
    }

    public static void registerItemProperty(Item pItem, ResourceLocation pName, ClampedItemPropertyFunction pProperty) {
        ItemProperties.register(pItem, pName, pProperty);
    }

    public static BakedModel loadModel(BlockRenderDispatcher dispatcher, ResourceLocation path) {
        return BakedModelManagerHelper.getModel(dispatcher.getBlockModelShaper().getModelManager(), path);
    }

    public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreen(MenuType<H> type, IronChestsClient.Factory<H, S> factory) {
        MenuScreens.register(type, factory::create);
    }

    public static <T extends BlockEntity> void registerEntityRenderer(BlockEntityType<T> type, BlockEntityRendererProvider<T> provider) {
        BlockEntityRenderers.register(type, provider);
    }
}
