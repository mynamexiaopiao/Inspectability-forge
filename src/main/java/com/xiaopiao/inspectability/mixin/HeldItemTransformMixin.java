package com.xiaopiao.inspectability.mixin;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.xiaopiao.inspectability.HeldItemTransformManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ItemInHandRenderer.class)
public class HeldItemTransformMixin {

    @Inject(method = "renderArmWithItem", at = @At("HEAD"))
    private void captureInitialTransformations(
            AbstractClientPlayer p_109372_, float p_109373_, float p_109374_, InteractionHand hand, float p_109376_, ItemStack p_109377_, float p_109378_,
            PoseStack poseStack, MultiBufferSource p_109380_, int p_109381_, CallbackInfo ci
    ) {
        if (!HeldItemTransformManager.isInspectorMode()) {
            poseStack.pushPose();
            HeldItemTransformManager.setInitialTransformations(poseStack.last().pose(), hand);
            poseStack.popPose();
        }
    }

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void modifyHeldItemRendering(
            AbstractClientPlayer player, float tickDelta, float pitch, InteractionHand hand, float swingProgress,
            ItemStack stack, float equipProgress, PoseStack poseStack,
            MultiBufferSource buffers, int light, CallbackInfo ci
    ) {
        if (HeldItemTransformManager.isInspectorMode()) {
            ci.cancel();

            HeldItemTransformManager.updateTransition();
            poseStack.pushPose();

            poseStack.translate(
                    HeldItemTransformManager.getOffsetX(),
                    HeldItemTransformManager.getOffsetY(),
                    HeldItemTransformManager.getOffsetZ()
            );

            float scale = HeldItemTransformManager.getScale();
            poseStack.scale(scale, scale, scale);

            poseStack.mulPose(Axis.YP.rotationDegrees(HeldItemTransformManager.getRotationY()));
            poseStack.mulPose(Axis.XP.rotationDegrees(HeldItemTransformManager.getRotationX()));
            poseStack.mulPose(Axis.ZP.rotationDegrees(HeldItemTransformManager.getRotationZ()));

            @Nullable Level level = player.level();

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    stack,
                    ItemDisplayContext.GUI,
                    light,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    buffers,
                    level,
                    0
            );

            poseStack.popPose();
        }
    }
}
