package com.yuti.dynamicskins.client.loading;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

public class DynamicOnlineTexture
{
    private int skintextureId = -1;

    private boolean isSlim = false;

    public DynamicOnlineTexture(BufferedImage bufferedimage)
    {
        this.load(bufferedimage);
    }

	public void load(BufferedImage bufferedimage) {
		parseTexture(bufferedimage);
	}
	
	@Nullable
	private void parseTexture(BufferedImage bufferedimage) {
		if(bufferedimage == null) {
			return;
		}
		
		int height = bufferedimage.getHeight();
		int width = bufferedimage.getWidth();
		if(height == 0 || width == 0 || (height % 32 != 0) || (width % 64 != 0) || height > 1280 || width > 1280) {
			return;
		}
		
        BufferedImage resized = getResizedImage(bufferedimage);
		BufferedImage skinImage = new BufferedImage(width, width, 2);
		Graphics graphics = skinImage.getGraphics();
        graphics.drawImage(resized, 0, 0, (ImageObserver)null);
        graphics.dispose();
		int[] imageDataFull = ((DataBufferInt)skinImage.getRaster().getDataBuffer()).getData();
		int widthQuart = width / 4;
        this.isSlim = checkForSlim(imageDataFull, width);
        setAreaOpaque(imageDataFull, width, 0, 0, widthQuart * 2, widthQuart);
        setAreaOpaque(imageDataFull, width, 0, widthQuart, width, widthQuart * 2);
        setAreaOpaque(imageDataFull, width, widthQuart, widthQuart * 3, widthQuart * 3, width);
        
		Minecraft.getMinecraft().addScheduledTask(() -> {
			TextureUtil.uploadTextureImageAllocate(this.getSkinTextureId(), skinImage, false, false);
		});

    }
	
	private BufferedImage getResizedImage(BufferedImage bufferedimage) {
		int height = bufferedimage.getHeight();
		int width = bufferedimage.getWidth();
		BufferedImage resized;
		if((height % width) != 0) {
			resized = new BufferedImage(width, width, 2);
			Graphics graphics = resized.getGraphics();
			graphics.drawImage(bufferedimage, 0, 0, (ImageObserver)null);
			int xMultiplicator = (height / 32);
			int yMultiplicator = (width / 64);
			graphics.setColor(new Color(0, 0, 0, 0));
            graphics.fillRect(0, height, width, height);
            graphics.drawImage(bufferedimage, 24 * xMultiplicator, 48 * yMultiplicator, 20 * xMultiplicator, 52 * yMultiplicator, 4 * xMultiplicator, 16 * yMultiplicator, 8 * xMultiplicator, 20 * yMultiplicator, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 28 * xMultiplicator, 48 * yMultiplicator, 24 * xMultiplicator, 52 * yMultiplicator, 8 * xMultiplicator, 16 * yMultiplicator, 12 * xMultiplicator, 20 * yMultiplicator, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 20 * xMultiplicator, 52 * yMultiplicator, 16 * xMultiplicator, 64 * yMultiplicator, 8 * xMultiplicator, 20 * yMultiplicator, 12 * xMultiplicator, 32 * yMultiplicator, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 24 * xMultiplicator, 52 * yMultiplicator, 20 * xMultiplicator, 64 * yMultiplicator, 4 * xMultiplicator, 20 * yMultiplicator, 8 * xMultiplicator, 32 * yMultiplicator, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 28 * xMultiplicator, 52 * yMultiplicator, 24 * xMultiplicator, 64 * yMultiplicator, 0 * xMultiplicator, 20 * yMultiplicator, 4 * xMultiplicator, 32 * yMultiplicator, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 32 * xMultiplicator, 52 * yMultiplicator, 28 * xMultiplicator, 64 * yMultiplicator, 12 * xMultiplicator, 20 * yMultiplicator, 16 * xMultiplicator, 32 * yMultiplicator, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 40 * xMultiplicator, 48 * yMultiplicator, 36 * xMultiplicator, 52 * yMultiplicator, 44 * xMultiplicator, 16 * yMultiplicator, 48 * xMultiplicator, 20 * yMultiplicator, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 44 * xMultiplicator, 48 * yMultiplicator, 40 * xMultiplicator, 52 * yMultiplicator, 48 * xMultiplicator, 16 * yMultiplicator, 52 * xMultiplicator, 20 * yMultiplicator, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 36 * xMultiplicator, 52 * yMultiplicator, 32 * xMultiplicator, 64 * yMultiplicator, 48 * xMultiplicator, 20 * yMultiplicator, 52 * xMultiplicator, 32 * yMultiplicator, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 40 * xMultiplicator, 52 * yMultiplicator, 36 * xMultiplicator, 64 * yMultiplicator, 44 * xMultiplicator, 20 * yMultiplicator, 48 * xMultiplicator, 32 * yMultiplicator, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 44 * xMultiplicator, 52 * yMultiplicator, 40 * xMultiplicator, 64 * yMultiplicator, 40 * xMultiplicator, 20 * yMultiplicator, 44 * xMultiplicator, 32 * yMultiplicator, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 48 * xMultiplicator, 52 * yMultiplicator, 44 * xMultiplicator, 64 * yMultiplicator, 52 * xMultiplicator, 20 * yMultiplicator, 56 * xMultiplicator, 32 * yMultiplicator, (ImageObserver)null);
            graphics.dispose();
            int[] imageData = ((DataBufferInt)resized.getRaster().getDataBuffer()).getData();
            setAreaTransparent(imageData, width, width, height, 0, width, height);
		}
		else {
			resized = bufferedimage;
		}
		
		return resized;
	}

    private boolean checkForSlim(int[]imageData, int width) {
        return checkAreaTransparent(imageData, width, 46, 47, 52, 63)
                && checkAreaTransparent(imageData, width, 54, 55, 20, 31);
    }

    private boolean checkAreaTransparent(int[]imageData, int size, int startX, int endX, int startY, int endY) {
        int regionStartX = (startX * (size / 64));
        int regionEndX = (endX * (size / 64));
        int regionStartY = (startY * (size / 64));
        int regionEndY = (endY * ( size / 64));
        for(int x = regionStartX; x <= regionEndX; x++) {
            for(int y = regionStartY; y <= regionEndY; y++) {
                int k = imageData[x + y * size];
                if(k >> 24 != 0x00) {
                    return false;
                }
            }
        }

        return true;
    }
	
	public void bindSkin() {
		GlStateManager.bindTexture(getSkinTextureId());
	}

    public boolean isSlim() {
        return isSlim;
    }

    public int getSkinTextureId() {
        if(skintextureId == -1 || !GL11.glIsTexture(skintextureId))
        {
        	skintextureId = GlStateManager.generateTexture();
        }
        return skintextureId;
    }
    
    private static void setAreaTransparent(int[] imageData, int imageHeight, int imageWidth, int x, int y, int width, int height)
    {
        for (int i = x; i < width; ++i)
        {
            for (int j = y; j < height; ++j)
            {
                int k = imageData[i + j * imageWidth];

                if ((k >> 24 & 255) < 128)
                {
                    return;
                }
            }
        }

        for (int l = x; l < width; ++l)
        {
            for (int i1 = y; i1 < height; ++i1)
            {
                imageData[l + i1 * imageWidth] &= 16777215;
            }
        }
    }

    private static void setAreaOpaque(int[] imageData, int imageWidth, int x, int y, int width, int height)
    {
        for (int i = x; i < width; ++i)
        {
            for (int j = y; j < height; ++j)
            {
                imageData[i + j * imageWidth] |= -16777216;
            }
        }
    }
}
