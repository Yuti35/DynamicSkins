package com.yuti.dynamicskins.common.capabilities;

import com.yuti.dynamicskins.client.loading.DynamicOnlineTexture;
import com.yuti.dynamicskins.client.loading.DynamicOnlineTextureManager;
import com.yuti.dynamicskins.common.network.PacketCapabilitySyncDynamicSkin;
import com.yuti.dynamicskins.common.network.PacketHandler;
import com.yuti.dynamicskins.server.utils.UtilsServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;
import java.util.concurrent.Callable;

public class DynamicSkinsCurrentCapability implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

    private EntityPlayer player;

    private String currentSkinURL;

    public static void register() {
        CapabilityManager.INSTANCE.register(DynamicSkinsCurrentCapability.class, new DynamicSkinsCurrentCapability.Storage(), new DynamicSkinsCurrentCapability.Factory());
    }

    public DynamicSkinsCurrentCapability(EntityPlayer player) {
        this.player = player;
    }

    public void sync()
    {
        PacketCapabilitySyncDynamicSkin packet = new PacketCapabilitySyncDynamicSkin(this.player.getEntityId(), this);

        if(!this.player.world.isRemote)
        {
            if(this.player instanceof EntityPlayerMP) {
                PacketHandler.INSTANCE.sendTo(packet, (EntityPlayerMP) this.player);
                Set<? extends EntityPlayer> players = UtilsServer.getPlayersTracking((EntityPlayerMP) this.player);
                for(EntityPlayer playerTarget : players) {
                    if(playerTarget instanceof EntityPlayerMP) {
                        PacketHandler.INSTANCE.sendTo(packet, (EntityPlayerMP) playerTarget);
                    }
                }
            }
        }
    }

    public static void resynchCapability(EntityPlayerMP player) {
        DynamicSkinsCurrentCapability capCurrentSkin = player.getCapability(DynamicSkinsCapabilities.DYNAMIC_SKINS_CURRENT, null);
        if(capCurrentSkin != null) {
            capCurrentSkin.sync();
        }
    }

    public void syncWithCapability(DynamicSkinsCurrentCapability cap) {
        this.currentSkinURL = cap.currentSkinURL;
    }

    public void syncWith(EntityPlayerMP target)
    {
        if(player != null && target != null) {
            if(!this.player.world.isRemote)
            {
                PacketCapabilitySyncDynamicSkin packetPlayer = new PacketCapabilitySyncDynamicSkin(this.player.getEntityId(), this);
                PacketHandler.INSTANCE.sendTo(packetPlayer, target);
            }
        }
    }

    public boolean hasSkin() {
        return this.currentSkinURL != null;
    }

    public String getCurrentSkinURL() {
        return currentSkinURL;
    }

    public void setCurrentSkinURL(String currentSkinURL) {
        this.currentSkinURL = currentSkinURL;
    }

    @SideOnly(Side.CLIENT)
    public DynamicOnlineTexture getDynamicSkinTexture() {
        if(this.currentSkinURL != null) {
            return DynamicOnlineTextureManager.loadUrl(this.currentSkinURL);
        }

        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        if(this.currentSkinURL != null) {
            compound.setString("currentSkinURL", this.currentSkinURL);
        }
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if(nbt.hasKey("currentSkinURL")) {
            this.currentSkinURL = nbt.getString("currentSkinURL");
        }
        else {
            this.currentSkinURL = null;
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return DynamicSkinsCapabilities.DYNAMIC_SKINS_CURRENT != null && capability == DynamicSkinsCapabilities.DYNAMIC_SKINS_CURRENT;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return DynamicSkinsCapabilities.DYNAMIC_SKINS_CURRENT != null && capability == DynamicSkinsCapabilities.DYNAMIC_SKINS_CURRENT ? (T) this : null;
    }

    public static class Storage implements Capability.IStorage<DynamicSkinsCurrentCapability> {

        @Override
        public NBTBase writeNBT(Capability<DynamicSkinsCurrentCapability> capability, DynamicSkinsCurrentCapability instance, EnumFacing side)

        {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<DynamicSkinsCurrentCapability> capability, DynamicSkinsCurrentCapability instance, EnumFacing side, NBTBase nbt)
        {
            instance.deserializeNBT((NBTTagCompound)nbt);
        }
    }

    public static class Factory implements Callable<DynamicSkinsCurrentCapability> {

        @Override
        public DynamicSkinsCurrentCapability call() throws Exception
        {
            return null;
        }
    }
}
