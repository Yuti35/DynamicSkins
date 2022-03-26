package com.yuti.dynamicskins.server.commands;

import com.google.common.collect.Lists;
import com.yuti.dynamicskins.DynamicSkins;
import com.yuti.dynamicskins.common.config.ModConfig;
import com.yuti.dynamicskins.common.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class CommandDynamicSkinConfig extends CommandBase {

    private static final List<String> options = Lists.newArrayList("skinslimit");

    @Override
    public String getName() {
        return "dynamicskinsconfig";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.dynamicskinsconfig";
    }

    @Override
    public List<String> getAliases() {
        return Lists.newArrayList("dskinsc");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length >= 1) {
            if(args[0].equals("skinslimit")) {
                if(args.length >= 2) {
                    String skinsLimit = args[1];
                    try {
                        int limit = Integer.parseInt(skinsLimit);
                        if(limit < 0) {
                            limit = 0;
                        }
                        ModConfig.server.skinsLimit = limit;
                        DynamicSkins.config.save();
                        Utils.sendValidMessageToEntity(sender, "dynamicskins.commands.config.limit.update", limit);
                        return;
                    }
                    catch (NumberFormatException e) {
                        Utils.sendErrorMessageToEntity(sender, "dynamicskins.commands.config.limit.notnumber", skinsLimit);
                        return;
                    }
                }
                else {
                    Utils.sendValidMessageToEntity(sender, "dynamicskins.commands.config.limit.current", ModConfig.server.skinsLimit);
                    return;
                }
            }
        }
        Utils.sendErrorMessageToEntity(sender, "commands.dynamicskinsconfig");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
                                          BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, options);
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
