package com.yuti.dynamicskins.client.loading;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

public class DynamicTextureLoadingUtils {
	
	private static boolean whitelistInitialized = false;
	private static Set<String> allowedHosts = new HashSet<>();
	
	@SideOnly(Side.CLIENT)
	public static void registerAllowedHosts(Set<String> hosts) {
		DynamicTextureLoadingUtils.whitelistInitialized = true;
		DynamicTextureLoadingUtils.allowedHosts = hosts;
	}
	
	@SideOnly(Side.CLIENT)
	public static boolean isHostAllowed(String host) {
		return DynamicTextureLoadingUtils.allowedHosts.contains(host);
	}
	
	@SideOnly(Side.CLIENT)
	public static boolean whitelistIsInitialized() {
		return whitelistInitialized;
	}

}
