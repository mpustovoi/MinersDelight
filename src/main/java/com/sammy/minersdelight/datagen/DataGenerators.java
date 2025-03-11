package com.sammy.minersdelight.datagen;

import com.sammy.malum.*;
import com.sammy.malum.datagen.block.*;
import com.sammy.malum.datagen.item.*;
import com.sammy.malum.datagen.lang.*;
import com.sammy.malum.datagen.recipe.*;
import net.minecraft.core.*;
import net.minecraft.data.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.common.data.*;
import net.neoforged.neoforge.data.event.*;

import java.util.concurrent.*;

@EventBusSubscriber(modid = MalumMod.MALUM, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        final boolean includeClient = event.includeClient();
        final boolean includeServer = event.includeServer();

        var registryDataProvider = new RegistryDataGenerator(output, provider);
        generator.addProvider(includeServer, registryDataProvider);
        var registryProvider = registryDataProvider.getRegistryProvider();
        generator.addProvider(includeServer, new MalumDataMaps(output, registryProvider));

        var itemModelsProvider = new MalumItemModels(output, helper);
        var blockTagsProvider = new MalumBlockTags(output, registryProvider, helper);

        generator.addProvider(includeClient, new MalumBlockStates(output, helper, itemModelsProvider));
        generator.addProvider(includeClient, itemModelsProvider);
        generator.addProvider(includeClient, new MalumLang(output));
        generator.addProvider(includeClient, new MalumSoundDatagen(output, helper));

        generator.addProvider(includeServer, blockTagsProvider);
        generator.addProvider(includeServer, new MalumBlockLootTables(output, registryProvider));


        generator.addProvider(includeServer, new MalumItemTags(output, provider, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(includeServer, new MalumBiomeTags(output, registryProvider, helper));
        generator.addProvider(includeServer, new MalumDamageTypeTags(output, registryProvider, helper));
        generator.addProvider(includeServer, new MalumEnchantmentTags(output, registryProvider, helper));


        generator.addProvider(includeServer, new MalumRecipes(output, registryProvider));

        generator.addProvider(includeServer, new MalumCuriosThings(output, helper, registryProvider));


    }
}
