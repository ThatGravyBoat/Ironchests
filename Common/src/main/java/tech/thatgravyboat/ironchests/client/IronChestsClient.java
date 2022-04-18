package tech.thatgravyboat.ironchests.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import tech.thatgravyboat.ironchests.Constants;
import tech.thatgravyboat.ironchests.common.chesttypes.ChestType;
import tech.thatgravyboat.ironchests.common.chesttypes.ChestTypes;
import tech.thatgravyboat.ironchests.common.items.DollyItem;
import tech.thatgravyboat.ironchests.common.registry.ItemRegistry;
import tech.thatgravyboat.ironchests.platform.ClientServices;

public class IronChestsClient {

    public static void init() {
        for (ChestType value : ChestTypes.TYPES.values()) {
            ClientServices.CLIENT.registerScreen(value.registries().getMenu().get(), ChestScreen::new);
            ClientServices.CLIENT.registerEntityRenderer(value.registries().getBlockEntity().get(), context -> new ChestRenderer<>(value));
            if (value.transparent()) ClientServices.CLIENT.setRenderLayer(value.registries().getBlock().get(), RenderType.translucent());
        }

        ClientServices.CLIENT.registerItemProperty(ItemRegistry.DIAMOND_DOLLY.get(), new ResourceLocation(Constants.MODID, "dolly_filled"),
                ((itemStack, clientLevel, livingEntity, i) -> DollyItem.getChestId(itemStack)));
        ClientServices.CLIENT.registerItemProperty(ItemRegistry.IRON_DOLLY.get(), new ResourceLocation(Constants.MODID, "dolly_filled"),
                ((itemStack, clientLevel, livingEntity, i) -> DollyItem.getChestId(itemStack)));
    }
}
