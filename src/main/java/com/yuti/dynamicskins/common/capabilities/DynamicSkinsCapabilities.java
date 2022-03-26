package com.yuti.dynamicskins.common.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class DynamicSkinsCapabilities {

    @CapabilityInject(DynamicSkinsStorageCapability.class)
    public static final Capability<DynamicSkinsStorageCapability> DYNAMIC_SKINS_STORAGE = null;

    @CapabilityInject(DynamicSkinsCurrentCapability.class)
    public static final Capability<DynamicSkinsCurrentCapability> DYNAMIC_SKINS_CURRENT = null;

    public static void registerCapabilities() {
        DynamicSkinsStorageCapability.register();
        DynamicSkinsCurrentCapability.register();
    }
}
