package com.yuti.dynamicskins.client.loading;

import com.yuti.dynamicskins.common.config.ModConfig;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DynamicOnlineTextureManager {

	@SideOnly(Side.CLIENT)
    public static DynamicOnlineTexture loadUrl(String url)
    {
		if(url == null) {
			return null;
		}

		if(!ModConfig.client.displayDynamicSkins) {
			return null;
		}

		if(!DynamicTextureLoadingUtils.whitelistIsInitialized()) {
			return null;
		}
		
		if(DynamicTextureCache.INSTANCE.isInFails(url)) {
			return null;
		}
		else if(!DynamicTextureCache.INSTANCE.loadTexture(url))
        {
			if(!DynamicTextureThread.isLoading()) {
				new DynamicTextureThread(url).start();
			}
            
            return null;
        }
        else
        {
            return DynamicTextureCache.INSTANCE.getTexture(url);
        }
    }
}
