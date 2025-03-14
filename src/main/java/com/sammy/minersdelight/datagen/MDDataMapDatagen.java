package com.sammy.minersdelight.datagen;

import com.sammy.minersdelight.content.data.*;
import com.sammy.minersdelight.setup.*;
import net.minecraft.core.*;
import net.minecraft.data.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.common.data.*;
import net.neoforged.neoforge.registries.datamaps.builtin.*;
import vectorwing.farmersdelight.common.registry.*;

import java.util.concurrent.*;

public class MDDataMapDatagen extends DataMapProvider {

    protected MDDataMapDatagen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        builder(MDDataMaps.CUP_VARIANT)
                .add(Holder.direct(Items.BUCKET), new CupConversionDataMap(MDItems.COPPER_CUP), false)
                .add(Holder.direct(Items.WATER_BUCKET), new CupConversionDataMap(MDItems.WATER_CUP), false)
                .add(Holder.direct(Items.MILK_BUCKET), new CupConversionDataMap(MDItems.MILK_CUP), false)
                .add(Holder.direct(Items.POWDER_SNOW_BUCKET), new CupConversionDataMap(MDItems.POWDERED_SNOW_CUP), false)
                .add(Holder.direct(Items.BEETROOT_SOUP), new CupConversionDataMap(MDItems.BEETROOT_SOUP_CUP), false)
                .add(Holder.direct(Items.MUSHROOM_STEW), new CupConversionDataMap(MDItems.MUSHROOM_STEW_CUP), false)
                .add(Holder.direct(Items.RABBIT_STEW), new CupConversionDataMap(MDItems.RABBIT_STEW_CUP), false)
                .add(Holder.direct(ModItems.BAKED_COD_STEW.get()), new CupConversionDataMap(MDItems.BAKED_COD_STEW_CUP), false)
                .add(Holder.direct(ModItems.NOODLE_SOUP.get()), new CupConversionDataMap(MDItems.NOODLE_SOUP_CUP), false)
                .add(Holder.direct(ModItems.BEEF_STEW.get()), new CupConversionDataMap(MDItems.BEEF_STEW_CUP), false)
                .add(Holder.direct(MDItems.CAVE_SOUP.get()), new CupConversionDataMap(MDItems.CAVE_SOUP_CUP), false)
                .add(Holder.direct(ModItems.CHICKEN_SOUP.get()), new CupConversionDataMap(MDItems.CHICKEN_SOUP_CUP), false)
                .add(Holder.direct(ModItems.FISH_STEW.get()), new CupConversionDataMap(MDItems.FISH_STEW_CUP), false)
                .add(Holder.direct(ModItems.HOT_COCOA.get()), new CupConversionDataMap(MDItems.HOT_COCOA_CUP), false)
                .add(Holder.direct(ModItems.PUMPKIN_SOUP.get()), new CupConversionDataMap(MDItems.PUMPKIN_SOUP_CUP), false)
                .add(Holder.direct(ModItems.VEGETABLE_SOUP.get()), new CupConversionDataMap(MDItems.VEGETABLE_SOUP_CUP), false);

        builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(MDItems.CAVE_CARROT, new Compostable(0.65f), false)
                .add(MDItems.BAKED_CAVE_CARROT, new Compostable(0.65f), false)
                .add(MDItems.GOSSYPIUM, new Compostable(0.3f), false);
    }
}