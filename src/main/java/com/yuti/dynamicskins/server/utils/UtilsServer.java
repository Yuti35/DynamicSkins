package com.yuti.dynamicskins.server.utils;

import com.yuti.dynamicskins.common.network.PacketHandler;
import com.yuti.dynamicskins.common.network.PacketSendDynamicLoadingHostWhitelist;
import com.yuti.dynamicskins.server.loaders.DynamicLoadingHostsManager;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UtilsServer {

    public static List<EntityPlayerMP> getPlayers() {
        PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
        if(playerList != null) {
            return playerList.getPlayers();
        }

        return new ArrayList<>();
    }

    public static void resyncTexturesHostsWhitelistWithClients() {
        PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getPlayerList();
        List<EntityPlayerMP> players = playerList.getPlayers();
        for(EntityPlayerMP player : players) {
            resyncTexturesHostsWhitelistWith(player);
        }
    }

    public static void resyncTexturesHostsWhitelistWith(EntityPlayerMP player) {
        Set<String> allowedHosts = DynamicLoadingHostsManager.getInstance().getAllowedHosts();
        PacketHandler.INSTANCE.sendTo(new PacketSendDynamicLoadingHostWhitelist(allowedHosts), player);
    }

    public static boolean hasAdminPermissions(EntityPlayer player) {
        return (FMLCommonHandler.instance()
                .getMinecraftServerInstance()
                .getPlayerList().canSendCommands(player.getGameProfile()));
    }

    public static Set<? extends EntityPlayer> getPlayersTracking(EntityPlayerMP player) {
        WorldServer world = player.getServerWorld();
        if(!world.isRemote) {
            EntityTracker tracker = world.getEntityTracker();
            return tracker.getTrackingPlayers(player);
        }
        return new HashSet<>();
    }
}
