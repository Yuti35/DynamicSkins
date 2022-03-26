package com.yuti.dynamicskins.server.handlers;

import com.yuti.dynamicskins.server.utils.UtilsServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@EventBusSubscriber
public class ServerEventHandler {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        UtilsServer.resyncTexturesHostsWhitelistWith((EntityPlayerMP) event.player);
    }

}
