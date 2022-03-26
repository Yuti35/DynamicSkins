package com.yuti.dynamicskins;

import com.yuti.dynamicskins.common.capabilities.DynamicSkinsCapabilities;
import com.yuti.dynamicskins.common.config.ModConfig;
import com.yuti.dynamicskins.common.network.PacketHandler;
import com.yuti.dynamicskins.server.commands.CommandsRegistry;
import com.yuti.dynamicskins.server.loaders.DynamicLoadingHostsManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = DynamicSkins.MODID, name = DynamicSkins.NAME, version = DynamicSkins.VERSION)
public class DynamicSkins
{
    public static final String MODID = "dynamicskins";
    public static final String NAME = "Dynamic Skins";
    public static final String VERSION = "1.0.0";

    public static ModConfig config = new ModConfig();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        PacketHandler.registerMessages(DynamicSkins.MODID);
        DynamicSkinsCapabilities.registerCapabilities();
    }
    @EventHandler
    public static void initServer(FMLServerStartingEvent event) {
        CommandsRegistry.registerCommands(event);
        DynamicLoadingHostsManager.getInstance().loadAllowedHosts();
    }

}
