package com.bware.mixin.client;

import com.bware.JujutsuCraft;
import com.bware.initializers.cca.EntityComponents;
import com.bware.initializers.registries.ModdedEffects;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class CEHudMixin extends DrawableHelper {

    @Unique
    private static final Identifier SORCERY_GUI_ICONS = new Identifier(JujutsuCraft.MOD_ID, "textures/gui/icons.png");

    private static final Identifier SORCERY_GUI_CE_BAR = new Identifier(JujutsuCraft.MOD_ID, "textures/gui/ce_bar.png");


    @Unique
    private static final int CE_X_OFFSET = 6;

    @Unique
    private static final int CE_Y_OFFSET = 7;

    @Unique
    private static final int CE_WIDTH = 110;

    @Unique
    private static final int CE_HEIGHT = 8;



    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    @Shadow
    private int scaledHeight;

    @Shadow
    private int scaledWidth;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    public abstract TextRenderer getTextRenderer();


    @Shadow
    @Final
    private Random random;

    @Shadow
    private ItemStack currentStack;

    @Shadow public abstract void renderExperienceBar(MatrixStack matrices, int x);

    @Inject(method = "renderExperienceBar", at = @At("TAIL"))
    public void jujutsu$renderManaModifier(MatrixStack matrices, int x, CallbackInfo ci) {
        PlayerEntity player = getCameraPlayer();


        EntityComponents.CURSED_ENERGY.maybeGet(player).ifPresent(cursedEnergyComponent -> {
            int mana = cursedEnergyComponent.getCE();
            int maxMana = cursedEnergyComponent.getCEMax();
            int xpBonus = cursedEnergyComponent.getXPBonus();

            if (xpBonus > 0) {
                String string = "+" + xpBonus;
                int k = x + 182 + 2;
                int l = this.scaledHeight - 31 + 1;
                this.getTextRenderer().draw(matrices, string, (float)(k + 1), (float)l, 0);
                this.getTextRenderer().draw(matrices, string, (float)(k - 1), (float)l, 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)(l + 1), 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)(l - 1), 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)l, 0x009295);
            }

            if (cursedEnergyComponent.shouldRender()) {
                int xPos = client.options.getMainArm().getValue() == Arm.LEFT ? scaledWidth - CE_X_OFFSET - CE_WIDTH : CE_X_OFFSET;
                int yPos = scaledHeight - CE_Y_OFFSET - CE_HEIGHT;

                String string = mana + "/" + maxMana;
                int k = xPos + 9;
                int l = (yPos - CE_HEIGHT - 3);
                this.getTextRenderer().draw(matrices, string, (float)(k + 1), (float)l, 0);
                this.getTextRenderer().draw(matrices, string, (float)(k - 1), (float)l, 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)(l + 1), 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)(l - 1), 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)l, 0x009295);
            }
        });
    }

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", shift = At.Shift.AFTER, ordinal = 2, target = "Lnet/minecraft/client/MinecraftClient;getProfiler()Lnet/minecraft/util/profiler/Profiler;"))
    private void jujutsu$renderStatusBars(MatrixStack matrices, CallbackInfo callbackInfo) {
        int xPos = client.options.getMainArm().getValue() == Arm.LEFT ? scaledWidth - CE_X_OFFSET - CE_WIDTH : CE_X_OFFSET;
        int yPos = scaledHeight - CE_Y_OFFSET - CE_HEIGHT;

        PlayerEntity player = getCameraPlayer();

        EntityComponents.CURSED_ENERGY.maybeGet(player).ifPresent(cursedEnergyComponent -> {
            if (!cursedEnergyComponent.shouldRender()) return;

            int ce = cursedEnergyComponent.getCE();
            int maxCE = cursedEnergyComponent.getCEMax();
            int vPos = cursedEnergyComponent.getCEFrame() * 6;

            RenderSystem.setShaderTexture(0, SORCERY_GUI_CE_BAR);
            RenderSystem.setShaderColor(1, 1, 1, 1.0f);
            drawTexture(matrices, xPos, yPos, CE_WIDTH - 2, CE_HEIGHT, CE_WIDTH, CE_HEIGHT, 230, 1944);
            drawTexture(matrices, xPos + 1, yPos + 1, 0, vPos, (int)((CE_WIDTH - 2) * Math.min((float)ce / maxCE, 1f)), CE_HEIGHT - 2, 230, 1944);
            if (player.hasStatusEffect(ModdedEffects.SUPPRESSED)) {
                drawTexture(matrices, xPos, yPos, CE_WIDTH - 2, 0, CE_WIDTH, CE_HEIGHT, 230, 1944);
            }
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
        });
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderVignetteOverlay(Lnet/minecraft/entity/Entity;)V"))
    private void jujutsu$render$vignette(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        renderInsanityVignette();
    }

    @Unique
    private void renderInsanityVignette() {
        PlayerEntity player = client.player;
        if (player == null) return;

    }

    @ModifyConstant(method = "renderHeldItemTooltip", constant = @Constant(intValue = 59))
    private int jujutsu$renderHeldItemTooltip$fixPosition(int constant) {

        if (this.client.interactionManager.hasStatusBars() && this.getCameraPlayer().getMaxHealth() > 20) {

            return (int) (constant + ((this.getCameraPlayer().getMaxHealth() - 1) / 20f) * 9);
        }

        return constant;
    }

    @Unique
    private void renderCustomVignette(float r, float g, float b, Identifier texture) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.setShaderColor(g, g, g, 1.0f);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(0.0, this.scaledHeight, -90.0).texture(0.0f, 1.0f).next();
        bufferBuilder.vertex(this.scaledWidth, this.scaledHeight, -90.0).texture(1.0f, 1.0f).next();
        bufferBuilder.vertex(this.scaledWidth, 0.0, -90.0).texture(1.0f, 0.0f).next();
        bufferBuilder.vertex(0.0, 0.0, -90.0).texture(0.0f, 0.0f).next();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
