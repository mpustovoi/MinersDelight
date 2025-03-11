package com.sammy.minersdelight.setup;

import com.sammy.minersdelight.*;
import com.sammy.minersdelight.content.data.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.datamaps.*;

public class MDDataMaps {

    public static final DataMapType<Item, CupConversionDataMap> CUP_VARIANT = DataMapType.builder(
            MinersDelightMod.path("cup_variant"),
            Registries.ITEM,
            CupConversionDataMap.CODEC
    ).build();

    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(CUP_VARIANT);
    }
}