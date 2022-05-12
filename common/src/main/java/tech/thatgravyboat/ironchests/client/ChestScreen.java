package tech.thatgravyboat.ironchests.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestMenu;

public class ChestScreen extends AbstractContainerScreen<GenericChestMenu> {

    private final ResourceLocation background;

    public ChestScreen(GenericChestMenu chestMenu, Inventory inventory, Component component) {
        super(chestMenu, inventory, component);
        this.passEvents = false;
        this.imageHeight = chestMenu.type.height();
        this.imageWidth = chestMenu.type.width();
        this.inventoryLabelY = 92 + ((chestMenu.type.rows() - 4) * 18);
        this.background = new ResourceLocation(IronChests.MODID, "textures/gui/chests/" + chestMenu.type.texture() + ".png");
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int i, int j, float f) {
        this.renderBackground(poseStack);
        super.render(poseStack, i, j, f);
        this.renderTooltip(poseStack, i, j);
    }

    protected void renderBg(@NotNull PoseStack poseStack, float f, int i, int j) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, background);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        int width = (int) Math.max(256, Math.ceil(this.imageWidth / 256f) * 256);
        int height = (int) Math.max(256, Math.ceil(this.imageHeight / 256f) * 256);
        int size = Math.max(width, height);

        blit(poseStack, x, y, 0, 0, this.imageWidth, this.imageHeight, size, size);
    }
}

