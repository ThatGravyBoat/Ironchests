package tech.thatgravyboat.ironchests;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tech.thatgravyboat.ironchests.client.IronChestsClient;
import tech.thatgravyboat.ironchests.common.chesttypes.ChestType;
import tech.thatgravyboat.ironchests.common.chesttypes.ChestTypes;

import java.util.Locale;

public class ForgeIronChestsClient {

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeIronChestsClient::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeIronChestsClient::onModelLoading);
    }

    public static void onModelLoading(ModelRegistryEvent event) {
        ForgeModelBakery.addSpecialModel(new ResourceLocation(Constants.MODID, "block/locked"));
        ForgeModelBakery.addSpecialModel(new ResourceLocation(Constants.MODID, "block/unlocked"));
        for (ChestType value : ChestTypes.TYPES.values()) {
            ForgeModelBakery.addSpecialModel(new ResourceLocation(Constants.MODID, "block/chests/"+ value.name().toLowerCase(Locale.ROOT) +"_chest_lid"));
            ForgeModelBakery.addSpecialModel(new ResourceLocation(Constants.MODID, "block/chests/"+ value.name().toLowerCase(Locale.ROOT) +"_chest_base"));
        }
    }

    public static void clientSetup(final FMLClientSetupEvent event) {
        IronChestsClient.init();
    }
}
