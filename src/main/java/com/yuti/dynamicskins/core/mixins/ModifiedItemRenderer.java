package com.yuti.dynamicskins.core.mixins;

import com.yuti.dynamicskins.client.loading.DynamicOnlineTexture;
import com.yuti.dynamicskins.common.capabilities.DynamicSkinsCapabilities;
import com.yuti.dynamicskins.common.capabilities.DynamicSkinsCurrentCapability;
import com.yuti.dynamicskins.common.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ModifiedItemRenderer {

    @Final
    @Shadow
    private Minecraft mc;

    @Inject(
            method = "renderArm",
            at = @At(
                    shift = At.Shift.AFTER,
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"
            )
    )
    private void bindTextureRedirectRenderArm(EnumHandSide p_187455_1_, CallbackInfo ci) {
        this.bindForDynamicSkinHand();
    }

    @Inject(
            method = "renderArmFirstPerson",
            at = @At(
                    shift = At.Shift.AFTER,
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"
            )
    )
    private void bindTextureRedirectRenderArmFirstPerson(float p_187456_1_, float p_187456_2_, EnumHandSide p_187456_3_, CallbackInfo ci) {
        this.bindForDynamicSkinHand();
    }

    public void bindForDynamicSkinHand() {
        //We use INJECT instead of REDIRECT to ensure compatibility with other mods
        //The default bindTexture from Minecraft will apply first before ours (which will eventually overrides it)
        if(ModConfig.client.displayDynamicSkins) {
            EntityPlayerSP player = mc.player;
            DynamicSkinsCurrentCapability currentCap = player.getCapability(DynamicSkinsCapabilities.DYNAMIC_SKINS_CURRENT, null);
            if(currentCap != null) {
                DynamicOnlineTexture dynamicSkin = currentCap.getDynamicSkinTexture();
                if(dynamicSkin != null) {
                    dynamicSkin.bindSkin();
                }
            }
        }
    }

}
