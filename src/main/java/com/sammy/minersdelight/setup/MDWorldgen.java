package com.sammy.minersdelight.setup;

import com.sammy.minersdelight.MinersDelightMod;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.neoforged.fml.common.Mod;

import java.util.List;

public class MDWorldgen {
    public static final class ConfiguredFeatures {
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_CAVE_CARROT = FeatureUtils.register("patch_cave_carrot", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MDBlocks.WILD_CAVE_CARROTS.get().defaultBlockState()))));
    }

    public static final class PlacedFeatures {
        public static final Holder<PlacedFeature> CAVE_CARROT = PlacementUtils.register("cave_carrot", ConfiguredFeatures.PATCH_CAVE_CARROT, List.of(
                RarityFilter.onAverageOnceEvery(32),
                CountPlacement.of(6),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.absolute(48)),
                BiomeFilter.biome()));
    }
}