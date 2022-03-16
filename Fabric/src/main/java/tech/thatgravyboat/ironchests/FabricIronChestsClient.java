package tech.thatgravyboat.ironchests;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.resources.ResourceLocation;
import tech.thatgravyboat.ironchests.client.IronChestsClient;
import tech.thatgravyboat.ironchests.common.chesttypes.ChestType;
import tech.thatgravyboat.ironchests.common.chesttypes.ChestTypes;

import java.util.Locale;

public class FabricIronChestsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(new ResourceLocation(Constants.MODID, "block/locked"));
            out.accept(new ResourceLocation(Constants.MODID, "block/unlocked"));

            for (ChestType value : ChestTypes.TYPES.values()) {
                out.accept(new ResourceLocation(Constants.MODID, "block/chests/"+ value.name().toLowerCase(Locale.ROOT) +"_chest_lid"));
                out.accept(new ResourceLocation(Constants.MODID, "block/chests/"+ value.name().toLowerCase(Locale.ROOT) +"_chest_base"));
            }
        });

        IronChestsClient.init();
    }
}
