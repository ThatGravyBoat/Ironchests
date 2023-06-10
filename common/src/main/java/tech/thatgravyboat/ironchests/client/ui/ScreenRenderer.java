package tech.thatgravyboat.ironchests.client.ui;

import net.minecraft.client.gui.GuiGraphics;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;

public interface ScreenRenderer {

    void render(GuiGraphics graphics, int screenWidth, int screenHeight);

    static ScreenRenderer of(ChestType type) {
        if (type.texture() == null) {
            return new GeneratedScreenRenderer(type);
        }
        return new TexturedScreenRenderer(type);
    }
}
