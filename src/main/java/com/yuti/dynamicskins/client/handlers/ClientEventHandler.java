package com.yuti.dynamicskins.client.handlers;

import com.yuti.dynamicskins.DynamicSkins;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class ClientEventHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(event.getModID().equals(DynamicSkins.MODID)) {
            DynamicSkins.config.save();
        }
    }

}
