package tech.thatgravyboat.ironchests.platform;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import tech.thatgravyboat.ironchests.platform.services.IClientHelper;

public class ForgeClientHelper implements IClientHelper {

    @Override
    public void registerItemProperty(Item pItem, ResourceLocation pName, ClampedItemPropertyFunction pProperty) {
        ItemProperties.register(pItem, pName, pProperty);
    }

    @Override
    public <T extends BlockEntity> void registerEntityRenderer(BlockEntityType<T> type, BlockEntityRendererProvider<T> provider) {
        BlockEntityRenderers.register(type, provider);
    }

    @Override
    public void setRenderLayer(Block block, RenderType type) {
        ItemBlockRenderTypes.setRenderLayer(block, type);
    }

    @Override
    public <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreen(MenuType<H> type, Factory<H, S> factory) {
        MenuScreens.register(type, factory::create);
    }
}
