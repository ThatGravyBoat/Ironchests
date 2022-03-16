package tech.thatgravyboat.ironchests;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tech.thatgravyboat.ironchests.platform.ForgeRegistryHelper;

@Mod(Constants.MODID)
public class ForgeIronChests {
    
    public ForgeIronChests() {
        CommonClass.init();
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ForgeRegistryHelper.ITEMS.register(modEventBus);
        ForgeRegistryHelper.BLOCKS.register(modEventBus);
        ForgeRegistryHelper.BLOCK_ENTITIES.register(modEventBus);
        ForgeRegistryHelper.MENU_TYPES.register(modEventBus);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ForgeIronChestsClient::init);
    }
}