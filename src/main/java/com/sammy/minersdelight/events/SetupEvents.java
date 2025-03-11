package com.sammy.minersdelight.events;

import com.sammy.minersdelight.setup.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.registries.datamaps.*;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class SetupEvents {

    @SubscribeEvent
    public static void registerDataMaps(RegisterDataMapTypesEvent event) {
        MDDataMaps.registerDataMapTypes(event);
    }
}