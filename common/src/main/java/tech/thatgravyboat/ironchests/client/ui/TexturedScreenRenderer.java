package tech.thatgravyboat.ironchests.client.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;

public record TexturedScreenRenderer(ResourceLocation texture, ChestType type) implements ScreenRenderer {

    public TexturedScreenRenderer(ChestType type) {
        this(new ResourceLocation(IronChests.MODID, "textures/gui/chests/" + type.texture() + ".png"), type);
    }

    @Override
    public void render(GuiGraphics graphics, int screenWidth, int screenHeight) {
        int x = (screenWidth - type.width()) / 2;
        int y = (screenHeight - type.height()) / 2;
        int width = (int) Math.max(256, Math.ceil(type.width() / 256f) * 256);
        int height = (int) Math.max(256, Math.ceil(type.height() / 256f) * 256);
        int size = Math.max(width, height);

        graphics.blit(texture, x, y, 0, 0, type.width(), type.height(), size, size);
    }
}
