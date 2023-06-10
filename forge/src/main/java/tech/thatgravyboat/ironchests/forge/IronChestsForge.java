package tech.thatgravyboat.ironchests.forge;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.client.forge.IronChestsForgeClient;
import tech.thatgravyboat.ironchests.common.registry.minecraft.ItemRegistry;

@Mod(IronChests.MODID)
public class IronChestsForge {

    public IronChestsForge() {
        IronChests.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> IronChestsForgeClient::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(IronChestsForge::onAddCreativeContents);
    }

    public static void onAddCreativeContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            ItemRegistry.CHESTS.boundStream().forEach(event::accept);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ItemRegistry.IRON_DOLLY.get());
            event.accept(ItemRegistry.DIAMOND_DOLLY.get());
            event.accept(ItemRegistry.BLANK_UPGRADE.get());
            event.accept(ItemRegistry.KEY.get());
            event.accept(ItemRegistry.KEY_RING.get());
            event.accept(ItemRegistry.LOCK.get());
            ItemRegistry.UPGRADES.boundStream().forEach(event::accept);
        }
    }
}
