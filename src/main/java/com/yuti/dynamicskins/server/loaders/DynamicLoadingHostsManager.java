package com.yuti.dynamicskins.server.loaders;

import com.yuti.dynamicskins.DynamicSkins;
import net.minecraftforge.fml.common.Loader;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class DynamicLoadingHostsManager {
	
	private static DynamicLoadingHostsManager instance;

	private Set<String> allowedHosts = new HashSet<>();

	private final String defaultWhitelistFileLocation = String.format("/assets/%s/configuration/dynamic_skins_hosts_whitelist_default.txt", DynamicSkins.MODID);

	private final String whiteListFileName = "dynamic_skins_hosts_whitelist.txt";

	private DynamicLoadingHostsManager() {}
	
	public static synchronized DynamicLoadingHostsManager getInstance() {
		if(instance == null) {
			instance = new DynamicLoadingHostsManager();
		}
		
		return instance;
	}

	private InputStream getDefaultHostWhitelistInputStream() {
		return  DynamicSkins.class.getResourceAsStream(defaultWhitelistFileLocation);
	}

	private InputStream getHostWhitelistInputStream() {
		File whitelistFile = getHostWhitelistFile();
		if(whitelistFile.exists()) {
			try {
				return new FileInputStream(whitelistFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private OutputStream getHostWhitelistOutputStream() {
		File whitelistFile = getHostWhitelistFile();
		try {
			return new FileOutputStream(whitelistFile);
		} catch (FileNotFoundException e) {
			return null;
		}
	}


	private File getHostWhitelistFile() {
		File config = Loader.instance().getConfigDir();
		Path path = config.toPath();
		Path toWhitelist = Paths.get(DynamicSkins.MODID, whiteListFileName);
		path = path.resolve(toWhitelist);
		return path.toFile();
	}

	private void whiteListFirstInit() {
		InputStream deFaultWhitelistStream = this.getDefaultHostWhitelistInputStream();
		if(deFaultWhitelistStream != null) {
			loadHostFromStream(deFaultWhitelistStream);
			try {
				deFaultWhitelistStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		updateWhitelistFile();
	}

	public void loadAllowedHosts() {
		InputStream whiteListStream = getHostWhitelistInputStream();
		if(whiteListStream == null) {
			this.whiteListFirstInit();
		}
		else {
			this.loadHostFromStream(whiteListStream);
			try {
				whiteListStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadHostFromStream(InputStream stream) {
		try {
			this.allowedHosts = new HashSet<>();
			if(stream != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				String host = reader.readLine();
				while(host != null) {
					if(!host.equals("")) {
						this.allowedHosts.add(host);
					}
					host = reader.readLine();
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void addHost(String host) {
		if(this.allowedHosts.add(host)) {
			updateWhitelistFile();
		}
	}

	public synchronized void removeHost(String host) {
		if(this.allowedHosts.remove(host)) {
			updateWhitelistFile();
		}		
	}

	public synchronized boolean isAllowed(String host) {
		if(this.allowedHosts != null) {
			return this.allowedHosts.contains(host);
		}

		return false;
	}

	public Set<String> getAllowedHosts() {
		return allowedHosts;
	}

	private synchronized void updateWhitelistFile() {
		try {
			OutputStream stream = this.getHostWhitelistOutputStream();
			if (stream != null) {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
				for (String host : this.allowedHosts) {
					String toWrite = host + "\n";
					writer.write(toWrite);
				}
				writer.close();
				stream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
