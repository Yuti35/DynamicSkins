package com.yuti.dynamicskins.common.config;

import com.yuti.dynamicskins.DynamicSkins;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;

@Config(modid = DynamicSkins.MODID, name= DynamicSkins.MODID+"/"+DynamicSkins.MODID)
public class ModConfig {

    public void save() {
        ConfigManager.sync(DynamicSkins.MODID, Config.Type.INSTANCE);
    }

    public static ClientConfig client = new ClientConfig();

    public static ServerConfig server = new ServerConfig();

    public static class ClientConfig {

        @Config.Comment("Enable or disable dynamic skins displaying and loading")
        @Config.LangKey("config.dynamicskins.displayDynamicSkins")
        public boolean displayDynamicSkins = true;
    }

    public static class ServerConfig {

        @Config.Comment("Number of skins allowed (per player)")
        @Config.LangKey("config.dynamicskins.server.skinsLimit")
        public int skinsLimit = 30;

    }

}
