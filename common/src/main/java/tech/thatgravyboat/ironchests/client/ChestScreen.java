package tech.thatgravyboat.ironchests.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.ironchests.client.ui.ScreenRenderer;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestMenu;

public class ChestScreen extends AbstractContainerScreen<GenericChestMenu> {

    private final ScreenRenderer renderer;

    public ChestScreen(GenericChestMenu chestMenu, Inventory inventory, Component component) {
        super(chestMenu, inventory, component);
        this.renderer = ScreenRenderer.of(chestMenu.type);
        this.imageHeight = chestMenu.type.height();
        this.imageWidth = chestMenu.type.width();
        this.inventoryLabelY = 92 + ((chestMenu.type.rows() - 4) * 18);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int i, int j, float f) {
        this.renderBackground(graphics);
        super.render(graphics, i, j, f);
        this.renderTooltip(graphics, i, j);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float f, int i, int j) {
        this.renderer.render(graphics, this.width, this.height);
    }
}

