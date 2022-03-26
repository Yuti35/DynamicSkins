package com.yuti.dynamicskins.client.loading;

import com.google.common.collect.Sets;
import com.yuti.dynamicskins.common.utils.Utils;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

public class DynamicTextureThread extends Thread
{
    private static final Set<String> FORMATS = Sets.newHashSet("image/png", "image/jpeg", "image/bmp");

    private static final long SIZE_LIMIT = 3000000;
    
    private static boolean LOADING = false;

    private final String url;
    
    public DynamicTextureThread(String url) {
    	this.url = url;
    }
    
    @Override
    public synchronized void start() {
    	if(!isLoading()) {
    		setLoading(true);
    		super.start();
    	}
    }

    @Override
    public void run()
    {
    		if(this.url == null) {
        		setLoading(false);
    			return;
    		}
    		
    		if(DynamicTextureCache.INSTANCE.loadTexture(url))
    		{
        		setLoading(false);
    			return;
    		}
    		
    		String host = Utils.getHostName(url);
    		if(host == null || !DynamicTextureLoadingUtils.isHostAllowed(host)) {
				DynamicTextureCache.INSTANCE.addFail(url);
        		setLoading(false);
    			return;
    		}
    		
    		try
    		{
    			URLConnection connection = new URL(url).openConnection();
    			connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322)");
    			if(!FORMATS.contains(connection.getContentType()))
    			{
    				DynamicTextureCache.INSTANCE.addFail(url);
    	    		setLoading(false);
    				return;
    			}
    			
    			long length = Long.parseLong(connection.getHeaderField("Content-Length"));
    			if(length > SIZE_LIMIT)
    			{
    				DynamicTextureCache.INSTANCE.addFail(url);
    	    		setLoading(false);
    				return;
    			}
    			
    			byte[] data = IOUtils.toByteArray(connection);
    			if(DynamicTextureCache.INSTANCE.addTexture(url, data))
    			{
    	    		setLoading(false);
    				return;
    			}
    		}
    		catch(Exception e) {
    			DynamicTextureCache.INSTANCE.addFail(url);
        		setLoading(false);
    		}
    		setLoading(false);
		
    }

	public static boolean isLoading() {
		return LOADING;
	}

	private static void setLoading(boolean loading) {
		DynamicTextureThread.LOADING = loading;
	}
}
