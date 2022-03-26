package com.yuti.dynamicskins.common.network;

import com.yuti.dynamicskins.client.loading.DynamicTextureLoadingUtils;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

public class PacketSendDynamicLoadingHostWhitelist implements IMessage {
	
	private Set<String> allowedHosts = new HashSet<String>();

	public PacketSendDynamicLoadingHostWhitelist() {

	}

	public PacketSendDynamicLoadingHostWhitelist(Set<String> allowedHosts) {
		this.allowedHosts = allowedHosts;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int size = buf.readInt();
		for(int i=0; i < size; i++) {
			String host = ByteBufUtils.readUTF8String(buf);
			this.allowedHosts.add(host);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.allowedHosts.size());
		for(String host : this.allowedHosts) {
			ByteBufUtils.writeUTF8String(buf, host);
		}
	}
	
	public static class Handler implements IMessageHandler<PacketSendDynamicLoadingHostWhitelist, IMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(PacketSendDynamicLoadingHostWhitelist message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler)
					.addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}

		@SideOnly(Side.CLIENT)
		void processMessage(PacketSendDynamicLoadingHostWhitelist message, MessageContext ctx) {
			if(message.allowedHosts != null) {
				DynamicTextureLoadingUtils.registerAllowedHosts(message.allowedHosts);
			}
		}
	}

}
