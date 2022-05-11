package tech.thatgravyboat.ironchests.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ChestBlock;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlock;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlockEntity;
import tech.thatgravyboat.ironchests.common.blocks.LockState;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;
import tech.thatgravyboat.ironchests.common.utils.ModUtils;

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
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        initModels(blockRenderer);

        if (!(chest.getBlockState().getBlock() instanceof GenericChestBlock)) return;

        poseStack.pushPose();

        RenderSystem.enableDepthTest();
        poseStack.translate(0.5D, 1D, 0.5D);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-chest.getBlockState().getValue(ChestBlock.FACING).toYRot()));
        poseStack.translate(-0.5D, -1D, -0.5D);

        poseStack.pushPose();
        //Pivot
        poseStack.translate(0,0.63,0.065);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-chest.getOpenness(f)));
        poseStack.translate(0,-0.63,-0.065);
        blockRenderer.getModelRenderer().renderModel(poseStack.last(), multiBufferSource.getBuffer(type.transparent() ? RenderType.translucentMovingBlock() : RenderType.cutout()), null, lid, 0.0F, 0.0F, 0.0F, i, j);
        poseStack.popPose();

        blockRenderer.getModelRenderer().renderModel(poseStack.last(), multiBufferSource.getBuffer(type.transparent() ? RenderType.translucentMovingBlock() : RenderType.cutout()), null, base, 0.0F, 0.0F, 0.0F, i, j);

        if (chest.getBlockState().getValue(GenericChestBlock.LOCK).equals(LockState.LOCKED)) {
            blockRenderer.getModelRenderer().renderModel(poseStack.last(), multiBufferSource.getBuffer(RenderType.cutout()), null, lockLocked, 1.0F, 1.0F, 1.0F, i, j);
        } else if (chest.getBlockState().getValue(GenericChestBlock.LOCK).equals(LockState.UNLOCKED)) {
            blockRenderer.getModelRenderer().renderModel(poseStack.last(), multiBufferSource.getBuffer(RenderType.cutout()), null, lockUnlocked, 1.0F, 1.0F, 1.0F, i, j);
        }

        if (type.renderItems() && (Minecraft.getInstance().options.graphicsMode.equals(GraphicsStatus.FABULOUS) || chest.getOpenness(f) > 0f)) renderItems(poseStack, chest, f, multiBufferSource, i, j);

        poseStack.popPose();
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

    private void renderItems(PoseStack poseStack, GenericChestBlockEntity blockEntity, float tickDelta, MultiBufferSource vertexConsumers, int light, int overlay) {
        NonNullList<ItemStack> inv = blockEntity.getItems();

        poseStack.translate(0.26,0.2, 0.26);

        if (blockEntity.getLevel() == null) return;

        int index = 0;
        for (int i = 0; i < 3; i++) {//Rows
            for (int j = 0; j < 3; j++) {//Row Length
                if (inv.get(index).getItem() != Items.AIR)
                    renderItem(0.24 * i, 0, 0.24 * j, inv.get(index), poseStack, blockEntity.getLevel().getGameTime() + tickDelta, vertexConsumers, light, overlay);
                index++;
                if (inv.get(index).getItem() != Items.AIR)
                    renderItem(0.24 * i, 0.2f, 0.24 * j, inv.get(index), poseStack, blockEntity.getLevel().getGameTime() + tickDelta, vertexConsumers, light, overlay);
                index++;
            }
        }

    }

    private void renderItem(double x, double y, double z, ItemStack stack, PoseStack matrices, float rotation, MultiBufferSource vertexConsumers, int light, int overlay) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        matrices.pushPose();
        matrices.translate(x, y, z);
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.mulPose(Vector3f.YP.rotationDegrees(rotation));
        renderer.renderStatic(stack, ItemTransforms.TransformType.GROUND, light, overlay, matrices, vertexConsumers, 0);
        matrices.popPose();
    }
}
