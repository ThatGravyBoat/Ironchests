package tech.thatgravyboat.ironchests.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.client.forge.IronChestsForgeClient;

@Mod(IronChests.MODID)
public class IronChestsForge {

    public IronChestsForge() {
        IronChests.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> IronChestsForgeClient::init);
    }
}
