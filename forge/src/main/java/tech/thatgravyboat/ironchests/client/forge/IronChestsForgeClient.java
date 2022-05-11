package tech.thatgravyboat.ironchests.client.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;
import tech.thatgravyboat.ironchests.client.IronChestsClient;
import tech.thatgravyboat.ironchests.common.registry.custom.ChestTypeRegistry;

import java.util.Locale;

public class IronChestsForgeClient {

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(IronChestsForgeClient::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(IronChestsForgeClient::onModelLoading);
    }

    public static void onModelLoading(ModelRegistryEvent event) {
        ForgeModelBakery.addSpecialModel(new ResourceLocation(IronChests.MODID, "block/locked"));
        ForgeModelBakery.addSpecialModel(new ResourceLocation(IronChests.MODID, "block/unlocked"));
        for (ChestType value : ChestTypeRegistry.INSTANCE.getChests().values()) {
            ForgeModelBakery.addSpecialModel(new ResourceLocation(IronChests.MODID, "block/chests/"+ value.name().toLowerCase(Locale.ROOT) +"_chest_lid"));
            ForgeModelBakery.addSpecialModel(new ResourceLocation(IronChests.MODID, "block/chests/"+ value.name().toLowerCase(Locale.ROOT) +"_chest_base"));
        }
    }

    public static void clientSetup(final FMLClientSetupEvent event) {
        IronChestsClient.init();
    }
}
