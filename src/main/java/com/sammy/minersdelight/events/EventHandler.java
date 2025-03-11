package com.sammy.minersdelight.events;

import com.sammy.minersdelight.*;
import com.sammy.minersdelight.setup.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.*;
import net.neoforged.event.*;
import net.neoforged.event.world.*;
import net.neoforged.eventbus.api.*;
import net.neoforged.fml.common.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = MinersDelightMod.MODID)
public class EventHandler {

    @SubscribeEvent
    public static void addWorldgenFeatures(BiomeLoadingEvent event) {
        if (!event.getCategory().equals(Biome.BiomeCategory.THEEND) && !event.getCategory().equals(Biome.BiomeCategory.NETHER))
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MDWorldgen.PlacedFeatures.CAVE_CARROT);
    }

}