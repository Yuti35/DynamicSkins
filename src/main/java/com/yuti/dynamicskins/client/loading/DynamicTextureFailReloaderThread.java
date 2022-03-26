package com.yuti.dynamicskins.client.loading;

public class DynamicTextureFailReloaderThread extends Thread {
	
	private String url;
	
	public DynamicTextureFailReloaderThread(String url) {
		super();
		this.url = url;
	}

	@Override
	public void run() {
		super.run();
		try {
			Thread.sleep(60000);
			DynamicTextureCache.INSTANCE.removeFail(url);
		} catch (InterruptedException e) {

		}
	}
}
