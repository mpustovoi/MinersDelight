package com.sammy.minersdelight.datagen;

import com.sammy.minersdelight.*;
import net.minecraft.core.*;
import net.minecraft.data.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.common.data.*;
import net.neoforged.neoforge.data.event.*;

import java.util.concurrent.*;

@EventBusSubscriber(modid = MinersDelightMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        boolean includeClient = event.includeClient();
        boolean includeServer = event.includeServer();

        generator.addProvider(includeServer, new MDDataMapDatagen(output, provider));
        generator.addProvider(includeClient, new MDLangDatagen(output));
        generator.addProvider(includeClient, new MDRecipeProvider(output, provider));
    }
}
