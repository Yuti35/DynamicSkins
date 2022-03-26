package com.yuti.dynamicskins.server.handlers;

import com.yuti.dynamicskins.DynamicSkins;
import com.yuti.dynamicskins.common.capabilities.DynamicSkinsCapabilities;
import com.yuti.dynamicskins.common.capabilities.DynamicSkinsCurrentCapability;
import com.yuti.dynamicskins.common.capabilities.DynamicSkinsStorageCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class DynamicSkinsCapabilitiesHandler {

    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof EntityPlayer)
        {
            event.addCapability(new ResourceLocation(DynamicSkins.MODID + ":CURRENT_SKIN"), new DynamicSkinsCurrentCapability((EntityPlayer) event.getObject()));
            event.addCapability(new ResourceLocation(DynamicSkins.MODID + ":SKINS_STORAGE"), new DynamicSkinsStorageCapability((EntityPlayer) event.getObject()));
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event)

    {
        if(event.getOriginal().hasCapability(DynamicSkinsCapabilities.DYNAMIC_SKINS_STORAGE, null))
        {
            DynamicSkinsStorageCapability capStorage = event.getOriginal().getCapability(DynamicSkinsCapabilities.DYNAMIC_SKINS_STORAGE, null);

            DynamicSkinsStorageCapability newCapStorage = event.getEntityPlayer().getCapability(DynamicSkinsCapabilities.DYNAMIC_SKINS_STORAGE, null);
            if(capStorage != null && newCapStorage != null) {
                newCapStorage.deserializeNBT(capStorage.serializeNBT());
            }
        }

        if(event.getOriginal().hasCapability(DynamicSkinsCapabilities.DYNAMIC_SKINS_CURRENT, null))

        {

            DynamicSkinsCurrentCapability capCurrentSkin = event.getOriginal().getCapability(DynamicSkinsCapabilities.DYNAMIC_SKINS_CURRENT, null);

            DynamicSkinsCurrentCapability newCapCurrentSkin = event.getEntityPlayer().getCapability(DynamicSkinsCapabilities.DYNAMIC_SKINS_CURRENT, null);

            if(capCurrentSkin != null && newCapCurrentSkin != null) {
                newCapCurrentSkin.deserializeNBT(capCurrentSkin.serializeNBT());
                newCapCurrentSkin.sync();
            }

        }

    }

    @SubscribeEvent
    public static void playerJoin(EntityJoinWorldEvent event)
    {
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayerMP) {
            if(!entity.world.isRemote) {
                DynamicSkinsCurrentCapability.resynchCapability((EntityPlayerMP) entity);
            }
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        EntityPlayer player = event.getEntityPlayer();
        if(player instanceof EntityPlayerMP) {
            Entity target = event.getTarget();
            if (target instanceof EntityPlayerMP) {
                DynamicSkinsCurrentCapability capCurrentSkin = target.getCapability(DynamicSkinsCapabilities.DYNAMIC_SKINS_CURRENT, null);
                if(capCurrentSkin != null) {
                    capCurrentSkin.syncWith((EntityPlayerMP) player);
                }
            }
        }
    }

}
