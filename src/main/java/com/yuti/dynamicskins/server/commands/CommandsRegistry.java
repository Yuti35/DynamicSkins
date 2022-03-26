package com.yuti.dynamicskins.server.commands;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandsRegistry {

    public static void registerCommands(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandDynamicSkinsWhitelist());
        event.registerServerCommand(new CommandDynamicSkins());
        event.registerServerCommand(new CommandDynamicSkinConfig());
    }
}
