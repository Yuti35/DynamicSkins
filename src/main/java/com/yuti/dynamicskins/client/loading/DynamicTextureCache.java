package com.yuti.dynamicskins.client.loading;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class DynamicTextureCache
{
    public static final DynamicTextureCache INSTANCE = new DynamicTextureCache();

    private final Map<String, DynamicOnlineTexture> mapTextures = new HashMap<>();
    
    private Set<String> setFail = new HashSet<>();

    @Nullable
    public DynamicOnlineTexture getTexture(String url)
    {
        if(url == null)
            return null;

        synchronized(this)
        {
            DynamicOnlineTexture texture = mapTextures.get(url);
            if(texture != null)
            {
                return texture;
            }
        }
        return null;
    }

	public boolean addTexture(String url, byte[] data) {
		synchronized (this) {
			try {
				if (!mapTextures.containsKey(url)) {
					ByteArrayInputStream bais = new ByteArrayInputStream(data);
					BufferedImage image = ImageIO.read(bais);
					DynamicOnlineTexture texture = new DynamicOnlineTexture(image);
					mapTextures.put(url, texture);
				}
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	public void addFail(String url, boolean reloadLater) {
		synchronized (this) {
			if(url != null && !this.setFail.contains(url) && reloadLater) {
				this.setFail.add(url);
				new DynamicTextureFailReloaderThread(url).start();
			}			
		}
	}
	
	public void addFail(String url) {
		this.addFail(url, true);
	}
	
	public void removeFail(String url) {
		synchronized (this) {
			if(url != null) {
				this.setFail.remove(url);
			}
		}
	}


    public boolean loadTexture(String url)
    {
        synchronized(this)
        {
            if(mapTextures.containsKey(url))
            {
                return true;
            }
        }

        return false;
    }
    
    public boolean isInCache(String url)
    {
        synchronized(this)
        {
            return mapTextures.containsKey(url);
        }
    }
    
    public boolean isInFails(String url) {
    	synchronized (this) {
			return this.setFail.contains(url);
		}
    }
    
    @SubscribeEvent
	public void onClientShutDown(FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
    	synchronized (this) {
    		this.setFail = new HashSet<>();
		}
	}
}
