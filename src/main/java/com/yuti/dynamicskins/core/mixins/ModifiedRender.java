package com.yuti.dynamicskins.core.mixins;

import com.yuti.dynamicskins.client.loading.DynamicOnlineTexture;
import com.yuti.dynamicskins.common.capabilities.DynamicSkinsCapabilities;
import com.yuti.dynamicskins.common.capabilities.DynamicSkinsCurrentCapability;
import com.yuti.dynamicskins.common.config.ModConfig;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Render.class)
public abstract class ModifiedRender<T extends Entity> {

    @Inject(at = @At("HEAD"), method = "bindEntityTexture(Lnet/minecraft/entity/Entity;)Z", cancellable = true)
    protected void bindEntityTexture(T entity, CallbackInfoReturnable<Boolean> callback) {
        if (entity instanceof EntityPlayer && ModConfig.client.displayDynamicSkins) {
            DynamicSkinsCurrentCapability currentCap = entity.getCapability(DynamicSkinsCapabilities.DYNAMIC_SKINS_CURRENT, null);
            if(currentCap != null) {
                DynamicOnlineTexture texture = currentCap.getDynamicSkinTexture();
                if(texture != null) {
                    texture.bindSkin();
                    callback.setReturnValue(true);
                }
            }
        }
    }

}
