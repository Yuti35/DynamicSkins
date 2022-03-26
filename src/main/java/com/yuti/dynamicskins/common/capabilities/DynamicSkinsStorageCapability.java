package com.yuti.dynamicskins.common.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

public class DynamicSkinsStorageCapability implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

    private EntityPlayer player;

    private Map<String, String> dynamicSkinsUrls;

    private String currentSkinName;

    public static void register() {
        CapabilityManager.INSTANCE.register(DynamicSkinsStorageCapability.class, new DynamicSkinsStorageCapability.Storage(), new DynamicSkinsStorageCapability.Factory());
    }

    public DynamicSkinsStorageCapability(EntityPlayer player) {
        this.player = player;
        this.dynamicSkinsUrls = new HashMap<>();
    }

    public Set<String> getSkinsNames() {
        return this.dynamicSkinsUrls.keySet();
    }

    public void addSkin(String skinName, String skinUrl) {
        this.dynamicSkinsUrls.put(skinName, skinUrl);
        if(this.isWearing(skinName)) {
            this.syncCurrentSkin();
        }
    }

    public void removeSkin(String skinName) {
        this.dynamicSkinsUrls.remove(skinName);
        if(this.isWearing(skinName)) {
            this.syncCurrentSkin();
        }
    }

    public boolean hasSkin(String skinName) {
        return this.dynamicSkinsUrls.containsKey(skinName);
    }

    public String getSkinURL(String skinName) {
        return this.dynamicSkinsUrls.get(skinName);
    }

    public String getCurrentSkinName() {
        return currentSkinName;
    }

    public void setCurrentSkinName(String currentSkinName) {
        this.currentSkinName = currentSkinName;
        this.syncCurrentSkin();
    }

    public boolean isWearingSkin() {
        return this.currentSkinName != null;
    }

    public boolean isWearing(String skinName) {
        return skinName.equals(this.currentSkinName);
    }

    public int getSkinsCount() {
        if(this.dynamicSkinsUrls != null) {
            return this.dynamicSkinsUrls.size();
        }

        return 0;
    }

    public void unset() {
        this.setCurrentSkinName(null);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        if(this.dynamicSkinsUrls != null) {
            NBTTagCompound mapTag = new NBTTagCompound();
            for(String skinName : this.dynamicSkinsUrls.keySet()) {
                mapTag.setString(skinName, this.dynamicSkinsUrls.get(skinName));
            }
            compound.setTag("dynamicSkinsUrls", mapTag);
        }
        if(this.currentSkinName != null) {
            compound.setString("currentSkinName", this.currentSkinName);
        }
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.dynamicSkinsUrls = new HashMap<>();
        if(nbt.hasKey("dynamicSkinsUrls")) {
            NBTBase baseTag = nbt.getTag("dynamicSkinsUrls");
            if(baseTag instanceof NBTTagCompound) {
                NBTTagCompound mapTag = (NBTTagCompound) baseTag;
                Set<String> keys = mapTag.getKeySet();
                for(String skinName : keys) {
                    String url = mapTag.getString(skinName);
                    this.dynamicSkinsUrls.put(skinName, url);
                }
            }
        }
        if(nbt.hasKey("currentSkinName")) {
            this.currentSkinName = nbt.getString("currentSkinName");
        }
        else {
            this.currentSkinName = null;
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return DynamicSkinsCapabilities.DYNAMIC_SKINS_STORAGE != null && capability == DynamicSkinsCapabilities.DYNAMIC_SKINS_STORAGE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return DynamicSkinsCapabilities.DYNAMIC_SKINS_STORAGE != null && capability == DynamicSkinsCapabilities.DYNAMIC_SKINS_STORAGE ? (T) this : null;
    }

    public void syncCurrentSkin() {
        if(this.player instanceof EntityPlayerMP) {
            DynamicSkinsCurrentCapability currentSkinCap = this.player.getCapability(DynamicSkinsCapabilities.DYNAMIC_SKINS_CURRENT, null);
            if(currentSkinCap != null) {
                currentSkinCap.setCurrentSkinURL(this.getSkinURL(this.currentSkinName));
                currentSkinCap.sync();
            }
        }
    }

    public static class Storage implements Capability.IStorage<DynamicSkinsStorageCapability> {

        @Override
        public NBTBase writeNBT(Capability<DynamicSkinsStorageCapability> capability, DynamicSkinsStorageCapability instance, EnumFacing side)

        {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<DynamicSkinsStorageCapability> capability, DynamicSkinsStorageCapability instance, EnumFacing side, NBTBase nbt)
        {
            instance.deserializeNBT((NBTTagCompound)nbt);
        }
    }

    public static class Factory implements Callable<DynamicSkinsStorageCapability> {

        @Override
        public DynamicSkinsStorageCapability call() throws Exception
        {
            return null;
        }
    }
}
