package com.yuti.dynamicskins.common.network;

import com.yuti.dynamicskins.common.capabilities.DynamicSkinsCurrentCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketCapabilitySyncDynamicSkin implements IMessage {

    public int playerId;
    public NBTTagCompound currentSkinCap;

    public PacketCapabilitySyncDynamicSkin(int playerId, DynamicSkinsCurrentCapability cap)
    {
        this.playerId = playerId;
        this.currentSkinCap = cap.serializeNBT();
    }

    public PacketCapabilitySyncDynamicSkin() {}


    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.playerId = buf.readInt();
        this.currentSkinCap = ByteBufUtils.readTag(buf);
    }


    @Override
    public void toBytes(ByteBuf buf)

    {
        buf.writeInt(this.playerId);
        ByteBufUtils.writeTag(buf, this.currentSkinCap);
    }


    public static class ClientHandler implements IMessageHandler<PacketCapabilitySyncDynamicSkin, IMessage> {



        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketCapabilitySyncDynamicSkin message, MessageContext ctx)

        {
            Minecraft.getMinecraft().addScheduledTask(new SchedulePacketCapabilitySyncDynamicSkin(message));

            return null;

        }

    }
}
