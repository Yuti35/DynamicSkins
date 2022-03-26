package com.yuti.dynamicskins.server.commands;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.yuti.dynamicskins.common.capabilities.DynamicSkinsCapabilities;
import com.yuti.dynamicskins.common.capabilities.DynamicSkinsStorageCapability;
import com.yuti.dynamicskins.common.config.ModConfig;
import com.yuti.dynamicskins.common.utils.Utils;
import com.yuti.dynamicskins.server.loaders.DynamicLoadingHostsManager;
import com.yuti.dynamicskins.server.utils.UtilsServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandDynamicSkins extends CommandBase {

    private static final List<String> options = Lists.newArrayList("list", "set", "remove", "wear", "unwear", "checkurl");

    @Override
    public String getName() {
        return "dynamicskins";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.dynamicskins";
    }

    @Override
    public List<String> getAliases() {
        return Lists.newArrayList("dskins");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
        if(sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            if(params.length >= 1 && options.contains(params[0])) {

                DynamicSkinsStorageCapability storage = player.getCapability(DynamicSkinsCapabilities.DYNAMIC_SKINS_STORAGE, null);

                if(storage != null) {
                    if(params[0].equals("list")) {
                        Set<String> skinsNames = storage.getSkinsNames();
                        if(skinsNames != null && !skinsNames.isEmpty()) {
                            String toShow = Joiner.on(", ").join(skinsNames);
                            Utils.sendMessageToEntity(player, toShow);
                        }
                        else {
                            Utils.sendMessageToEntity(player, "dynamicskins.commands.dynamicskins.noskins");
                        }
                        return;
                    }
                    else if(params[0].equals("unwear")) {
                        if(storage.isWearingSkin()) {
                            Utils.sendValidMessageToEntity(player, "dynamicskins.commands.dynamicskins.unwear", storage.getCurrentSkinName());
                            storage.unset();
                        }
                        else {
                            Utils.sendErrorMessageToEntity(player, "dynamicskins.commands.dynamicskins.nowear");
                        }
                        return;
                    }
                    else if(params.length >= 2) {
                        String skinName = params[1];
                        if(params[0].equals("wear")) {
                            if(storage.hasSkin(skinName)) {
                                String skinUrl = storage.getSkinURL(skinName);
                                if(checkHostAllowed(player, skinUrl)) {
                                    storage.setCurrentSkinName(skinName);
                                    Utils.sendValidMessageToEntity(player, "dynamicskins.commands.dynamicskins.wear", skinName);
                                }
                            }
                            else {
                                Utils.sendErrorMessageToEntity(player, "dynamicskins.commands.dynamicskins.notexist", skinName);
                            }
                            return;
                        }
                        else if(params[0].equals("remove")) {
                            if(storage.hasSkin(skinName)) {
                                storage.removeSkin(skinName);
                                Utils.sendValidMessageToEntity(player, "dynamicskins.commands.dynamicskins.removed", skinName);
                            }
                            else {
                                Utils.sendErrorMessageToEntity(player, "dynamicskins.commands.dynamicskins.notexist", skinName);
                            }
                            return;
                        }
                        else if(params[0].equals("checkurl")) {
                            if(storage.hasSkin(skinName)) {
                                Utils.sendMessageURLToEntity(player, storage.getSkinURL(skinName));
                            }
                            else {
                                Utils.sendErrorMessageToEntity(player, "dynamicskins.commands.dynamicskins.notexist", skinName);
                            }
                            return;
                        }
                        else if(params.length >= 3) {
                            if(params[0].equals("set")) {
                                String skinUrl = params[2];
                                if(checkHostAllowed(player, skinUrl)) {
                                    boolean flagStorage = true;
                                    if(storage.isWearing(skinName)) {
                                        Utils.sendValidMessageToEntity(player, "dynamicskins.commands.dynamicskins.updated", skinName);
                                    }
                                    else {
                                        if(!UtilsServer.hasAdminPermissions(player) && storage.getSkinsCount() >= ModConfig.server.skinsLimit) {
                                            flagStorage = false;
                                            Utils.sendErrorMessageToEntity(player, "dynamicskins.commands.dynamicskins.limit", ModConfig.server.skinsLimit);
                                        }
                                        else {
                                            Utils.sendValidMessageToEntity(player, "dynamicskins.commands.dynamicskins.created", skinName);
                                        }
                                    }
                                    if(flagStorage) {
                                        storage.addSkin(skinName, skinUrl);
                                    }
                                }
                                return;
                            }
                        }
                    }
                }
            }
            Utils.sendErrorMessageToEntity(player, "commands.dynamicskins");
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    private boolean checkHostAllowed(EntityPlayer player, String url) {
        String host = Utils.getHostName(url);
        if(host == null) {
            Utils.sendErrorMessageToEntity(player, "dynamicskins.commands.dynamicskins.host.invalid");
            return false;
        }
        DynamicLoadingHostsManager dynamicLoadingHostsManager = DynamicLoadingHostsManager.getInstance();
        if(!dynamicLoadingHostsManager.isAllowed(host)) {
            Utils.sendErrorMessageToEntity(player, "dynamicskins.commands.dynamicskins.host.notwhitelisted", host);
            if(UtilsServer.hasAdminPermissions(player)) {
                Utils.sendInfoMessageToEntity(player, "dynamicskins.commands.dynamicskins.host.opadvice", String.format("/dynamicskinswhitelist add %s", host));
            }
            return false;
        }
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
                                          BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, options);
        }

        if(args.length == 2 && (args[0].equals("set") || args[0].equals("remove")
                || args[0].equals("wear") || args[0].equals("checkurl"))) {
            if(sender instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) sender;
                DynamicSkinsStorageCapability storage = player.getCapability(DynamicSkinsCapabilities.DYNAMIC_SKINS_STORAGE, null);
                if(storage != null) {
                    Set<String> skinsNames = storage.getSkinsNames();
                    if(skinsNames != null) {
                        return getListOfStringsMatchingLastWord(args, skinsNames);
                    }
                }
            }
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }

}
