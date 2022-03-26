package com.yuti.dynamicskins.core.mixins;

import com.yuti.dynamicskins.client.loading.DynamicOnlineTexture;
import com.yuti.dynamicskins.common.capabilities.DynamicSkinsCapabilities;
import com.yuti.dynamicskins.common.capabilities.DynamicSkinsCurrentCapability;
import com.yuti.dynamicskins.common.config.ModConfig;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class ModifiedAbstractClientPlayer {

    @Inject(at = @At("HEAD"), method = "getSkinType", cancellable = true)
    protected void injectionGetSkinType(CallbackInfoReturnable<String> callback) {
        if(ModConfig.client.displayDynamicSkins) {
            AbstractClientPlayer entity = (AbstractClientPlayer) ((Object) this);
            DynamicSkinsCurrentCapability currentCap = entity.getCapability(DynamicSkinsCapabilities.DYNAMIC_SKINS_CURRENT, null);
            if(currentCap != null) {
                DynamicOnlineTexture texture = currentCap.getDynamicSkinTexture();
                if(texture != null) {
                    if(texture.isSlim()) {
                        callback.setReturnValue("slim");
                    }
                    else {
                        callback.setReturnValue("default");
                    }
                }
            }
        }
    }

}
