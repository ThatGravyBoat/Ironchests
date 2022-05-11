package tech.thatgravyboat.ironchests.client.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.resources.ResourceLocation;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;
import tech.thatgravyboat.ironchests.client.IronChestsClient;
import tech.thatgravyboat.ironchests.common.registry.custom.ChestTypeRegistry;

import java.util.Locale;

public class IronChestsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(new ResourceLocation(IronChests.MODID, "block/locked"));
            out.accept(new ResourceLocation(IronChests.MODID, "block/unlocked"));

            for (ChestType value : ChestTypeRegistry.INSTANCE.getChests().values()) {
                out.accept(new ResourceLocation(IronChests.MODID, "block/chests/"+ value.name().toLowerCase(Locale.ROOT) +"_chest_lid"));
                out.accept(new ResourceLocation(IronChests.MODID, "block/chests/"+ value.name().toLowerCase(Locale.ROOT) +"_chest_base"));
            }
        });

        IronChestsClient.init();
    }
}
