package com.yuti.dynamicskins.common.network;

import com.yuti.dynamicskins.common.capabilities.DynamicSkinsCapabilities;
import com.yuti.dynamicskins.common.capabilities.DynamicSkinsCurrentCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SchedulePacketCapabilitySyncDynamicSkin implements Runnable {

    private PacketCapabilitySyncDynamicSkin message;

    public SchedulePacketCapabilitySyncDynamicSkin(PacketCapabilitySyncDynamicSkin message)

    {
        this.message = message;
    }

    @Override
    public void run()
    {
        World world = Minecraft.getMinecraft().world;
        if (world==null) return;
        Entity p = world.getEntityByID(message.playerId);
        if (p instanceof EntityPlayer) {
            DynamicSkinsCurrentCapability cap = p.getCapability(DynamicSkinsCapabilities.DYNAMIC_SKINS_CURRENT, null);
            if(cap != null) {
                cap.deserializeNBT(message.currentSkinCap);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private EntityPlayer getPlayer()
    {
        return Minecraft.getMinecraft().player;

    }
}
