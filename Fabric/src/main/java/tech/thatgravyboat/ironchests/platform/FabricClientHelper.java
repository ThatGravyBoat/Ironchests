package tech.thatgravyboat.ironchests.platform;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import tech.thatgravyboat.ironchests.platform.services.IClientHelper;

public class FabricClientHelper implements IClientHelper {
    @Override
    public void registerItemProperty(Item pItem, ResourceLocation pName, ClampedItemPropertyFunction pProperty) {
        FabricModelPredicateProviderRegistry.register(pItem, pName, pProperty);
    }

    @Override
    public <T extends BlockEntity> void registerEntityRenderer(BlockEntityType<T> type, BlockEntityRendererProvider<T> provider) {
        BlockEntityRendererRegistry.register(type, provider);
    }

    @Override
    public void setRenderLayer(Block block, RenderType type) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, type);
    }

    @Override
    public <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreen(MenuType<H> type, Factory<H, S> factory) {
        ScreenRegistry.register(type, factory::create);
    }
}
