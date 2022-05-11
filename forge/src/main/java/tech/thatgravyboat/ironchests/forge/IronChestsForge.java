package tech.thatgravyboat.ironchests.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.client.forge.IronChestsForgeClient;
import tech.thatgravyboat.ironchests.common.registry.minecraft.forge.BlockRegistryImpl;
import tech.thatgravyboat.ironchests.common.registry.minecraft.forge.ItemRegistryImpl;
import tech.thatgravyboat.ironchests.common.registry.minecraft.forge.MenuRegistryImpl;

@Mod(IronChests.MODID)
public class IronChestsForge {

    public IronChestsForge() {
        IronChests.init();
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemRegistryImpl.ITEMS.register(modEventBus);
        BlockRegistryImpl.BLOCKS.register(modEventBus);
        BlockRegistryImpl.BLOCK_ENTITIES.register(modEventBus);
        MenuRegistryImpl.MENU_TYPES.register(modEventBus);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> IronChestsForgeClient::init);
    }
}
