package tech.thatgravyboat.ironchests.client.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;

public record GeneratedScreenRenderer(ChestType type) implements ScreenRenderer {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(IronChests.MODID, "textures/gui/base.png");
    private static final ResourceLocation SLOT = new ResourceLocation(IronChests.MODID, "textures/gui/slot.png");

    @Override
    public void render(GuiGraphics graphics, int screenWidth, int screenHeight) {
        int x = (screenWidth - type.width()) / 2;
        int y = (screenHeight - type.height()) / 2;

        graphics.blitNineSliced(BACKGROUND, x, y, type.width(), type.height(), 4, 4, 4, 4, 256, 256, 0, 0);

        x -= 1;
        y -= 1;

        for(int row = 0; row < type.rows(); ++row) {
            for (int slot = 0; slot < type.length(); ++slot) {
                graphics.blit(SLOT, x + type.menuOffset() + slot * 18, y + 18 + row * 18, 0, 0, 18, 18);
            }
        }

        int k = 1 + (type.rows() - 4) * 18;

        for(int row = 0; row < 3; ++row) {
            for(int slot = 0; slot < 9; ++slot) {
                graphics.blit(SLOT, x + type.inventoryOffset() + slot * 18, y + 103 + row * 18 + k, 0, 0, 18, 18);
            }
        }

        for(int slot = 0; slot < 9; ++slot) {
            graphics.blit(SLOT, x + type.inventoryOffset() + slot * 18, y + 161 + k, 0, 0, 18, 18);
        }
    }
}
