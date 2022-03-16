package tech.thatgravyboat.ironchests.platform.services;

import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

public interface IModelHelper {

    BakedModel loadModel(BlockRenderDispatcher dispatcher, ResourceLocation path);
}
