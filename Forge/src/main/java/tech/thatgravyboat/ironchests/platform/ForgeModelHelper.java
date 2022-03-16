package tech.thatgravyboat.ironchests.platform;

import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import tech.thatgravyboat.ironchests.platform.services.IModelHelper;

public class ForgeModelHelper implements IModelHelper {
    @Override
    public BakedModel loadModel(BlockRenderDispatcher dispatcher, ResourceLocation path) {
        return dispatcher.getBlockModelShaper().getModelManager().getModel(path);
    }
}
