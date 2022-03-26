package com.yuti.dynamicskins.server.commands;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.yuti.dynamicskins.common.utils.Utils;
import com.yuti.dynamicskins.server.loaders.DynamicLoadingHostsManager;
import com.yuti.dynamicskins.server.utils.UtilsServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandDynamicSkinsWhitelist extends CommandBase {
	
	private static final List<String> options = Lists.newArrayList("add", "remove", "reload");

	@Override
	public String getName() {
		return "dynamicskinswhitelist";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.dynamicskinswhitelist";
	}

	@Override
	public List<String> getAliases() {
		return Lists.newArrayList("dskinsw");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		DynamicLoadingHostsManager dynamicLoadingHostsManager = DynamicLoadingHostsManager.getInstance();
		Set<String> hosts = dynamicLoadingHostsManager.getAllowedHosts();
		if(args.length == 0) {
			if(!hosts.isEmpty()) {
				String toShow = Joiner.on(", ").join(hosts);
				Utils.sendMessageToEntity(sender, toShow);
			}
			else {
				Utils.sendMessageToEntity(sender, "dynamicskins.commands.whitelist.nohosts");
			}
			return;
		}
		else {
			if(args[0].equals("reload")) {
				dynamicLoadingHostsManager.loadAllowedHosts();
				UtilsServer.resyncTexturesHostsWhitelistWithClients();
				Utils.sendValidMessageToEntity(sender, "dynamicskins.commands.whitelist.reloaded");
				return;
			}
			else if(args.length >= 2) {
				String hostName = args[1];
				if(args[0].equals("add")) {
					if(!hosts.contains(hostName)) {
						dynamicLoadingHostsManager.addHost(hostName);
						UtilsServer.resyncTexturesHostsWhitelistWithClients();
						Utils.sendValidMessageToEntity(sender, "dynamicskins.commands.whitelist.added", hostName);
					}
					else {
						Utils.sendErrorMessageToEntity(sender, "dynamicskins.commands.whitelist.duplication", hostName);
					}
					return;
				}
				else if(args[0].equals("remove")) {
					if(hosts.contains(hostName)) {
						dynamicLoadingHostsManager.removeHost(hostName);
						UtilsServer.resyncTexturesHostsWhitelistWithClients();
						Utils.sendValidMessageToEntity(sender, "dynamicskins.commands.whitelist.removed", hostName);
					}
					else {
						Utils.sendErrorMessageToEntity(sender, "dynamicskins.commands.whitelist.notfound", hostName);
					}
					return;
				}
			}
		}

		Utils.sendErrorMessageToEntity(sender, "commands.dynamicskinswhitelist");
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		if (args.length == 1) {
			return getListOfStringsMatchingLastWord(args, options);
		}
		
		if(args.length == 2 && args[0].equals("remove")) {
			DynamicLoadingHostsManager dynamicLoadingHostsManager = DynamicLoadingHostsManager.getInstance();
			Set<String> hosts = dynamicLoadingHostsManager.getAllowedHosts();
			return getListOfStringsMatchingLastWord(args, hosts);
		}
		
		return super.getTabCompletions(server, sender, args, targetPos);
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
}
