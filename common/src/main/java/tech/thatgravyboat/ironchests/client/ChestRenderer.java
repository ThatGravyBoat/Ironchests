package tech.thatgravyboat.ironchests.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.api.chesttype.ChestBlockType;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlock;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlockEntity;
import tech.thatgravyboat.ironchests.common.blocks.LockState;

import java.util.Locale;

public class ChestRenderer<T extends GenericChestBlockEntity> implements BlockEntityRenderer<T> {

    private BakedModel lid;
    private BakedModel base;
    private static BakedModel lockUnlocked;
    private static BakedModel lockLocked;

    private final ChestType type;

    public ChestRenderer(ChestType type) {
        this.type = type;
    }

    @Override
    public void render(@NotNull T chest, float f, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, int j) {
        ProfilerFiller profiler = Minecraft.getInstance().getProfiler();
        profiler.push("ironchestRenderer");
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        initModels(blockRenderer);

        if (!(chest.getBlockState().getBlock() instanceof GenericChestBlock)) return;

        poseStack.pushPose();

        RenderSystem.enableDepthTest();
        poseStack.translate(0.5D, 1D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-chest.getBlockState().getValue(GenericChestBlock.getFacingProperty(type)).toYRot()));
        poseStack.translate(-0.5D, -1D, -0.5D);

        poseStack.pushPose();
        //Pivot
        poseStack.translate(0, 0.63, 0.065);
        poseStack.mulPose(Axis.XP.rotationDegrees(-chest.getOpenness(f)));
        poseStack.translate(0, -0.63, -0.065);
        if (type.blockType() == ChestBlockType.CHEST) {
            RenderType renderType = type.transparent() ? Sheets.translucentCullBlockSheet() : Sheets.cutoutBlockSheet();
            renderBlock(poseStack, multiBufferSource, renderType, lid, i);
            poseStack.popPose();

            renderBlock(poseStack, multiBufferSource, renderType, base, i);

            if (chest.getBlockState().getValue(GenericChestBlock.LOCK).equals(LockState.LOCKED)) {
                renderBlock(poseStack, multiBufferSource, Sheets.cutoutBlockSheet(), lockLocked, i);
            } else if (chest.getBlockState().getValue(GenericChestBlock.LOCK).equals(LockState.UNLOCKED)) {
                renderBlock(poseStack, multiBufferSource, Sheets.cutoutBlockSheet(), lockUnlocked, i);
            }
        } else {
            poseStack.popPose();
        }

        profiler.push("renderItems");
        if (type.renderItems() && (Minecraft.getInstance().options.graphicsMode().get().equals(GraphicsStatus.FABULOUS) || chest.getOpenness(f) > 0f || type.blockType() != ChestBlockType.CHEST)) renderItems(poseStack, chest, f, multiBufferSource, i);
        profiler.pop();

        poseStack.popPose();
        profiler.pop();
    }

    private static void renderBlock(PoseStack poseStack, MultiBufferSource multiBufferSource, RenderType renderType, BakedModel model, int i) {
        Minecraft.getInstance()
                .getBlockRenderer()
                .getModelRenderer()
                .renderModel(poseStack.last(), multiBufferSource.getBuffer(renderType), null, model, 1.0F, 1.0F, 1.0F, i, OverlayTexture.NO_OVERLAY);
    }

    private void initModels(BlockRenderDispatcher dispatcher) {
        if (base == null)
            base = IronChestsClient.loadModel(dispatcher, new ResourceLocation(IronChests.MODID, "block/chests/"+ type.name().toLowerCase(Locale.ROOT) +"_chest_base"));
        if (lid == null)
            lid = IronChestsClient.loadModel(dispatcher, new ResourceLocation(IronChests.MODID, "block/chests/"+ type.name().toLowerCase(Locale.ROOT) +"_chest_lid"));
        checkAndUpdateLocks(dispatcher);
    }

    private static void checkAndUpdateLocks(BlockRenderDispatcher dispatcher) {
        if (lockUnlocked == null)
            ChestRenderer.lockUnlocked = IronChestsClient.loadModel(dispatcher, new ResourceLocation(IronChests.MODID, "block/unlocked"));
        if (lockLocked == null)
            ChestRenderer.lockLocked = IronChestsClient.loadModel(dispatcher, new ResourceLocation(IronChests.MODID, "block/locked"));
    }

    private void renderItems(PoseStack poseStack, GenericChestBlockEntity blockEntity, float tickDelta, MultiBufferSource vertexConsumers, int light) {
        NonNullList<ItemStack> inv = blockEntity.getItems();

        poseStack.translate(0.26,0.2, 0.26);

        if (blockEntity.getLevel() == null) return;

        int index = 0;
        for (int i = 0; i < 3; i++) {//Rows
            for (int j = 0; j < 3; j++) {//Row Length
                if (inv.get(index).getItem() != Items.AIR)
                    renderItem(0.24 * i, 0, 0.24 * j, inv.get(index), poseStack, blockEntity.getLevel().getGameTime() + tickDelta, vertexConsumers, light);
                index++;
                if (inv.get(index).getItem() != Items.AIR)
                    renderItem(0.24 * i, 0.2f, 0.24 * j, inv.get(index), poseStack, blockEntity.getLevel().getGameTime() + tickDelta, vertexConsumers, light);
                index++;
            }
        }

    }

    private void renderItem(double x, double y, double z, ItemStack stack, PoseStack matrices, float rotation, MultiBufferSource vertexConsumers, int light) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        matrices.pushPose();
        matrices.translate(x, y, z);
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.mulPose(Axis.YP.rotationDegrees(rotation));
        renderer.renderStatic(stack, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, Minecraft.getInstance().level, 0);
        matrices.popPose();
    }
}
