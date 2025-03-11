package com.sammy.minersdelight.setup;

import net.minecraft.world.effect.*;
import net.minecraft.world.food.*;
import vectorwing.farmersdelight.common.registry.*;

public class MDFoodValues {

    public static final FoodProperties CAVE_CARROT = new FoodProperties.Builder().nutrition(3).saturationModifier(0.25f).build();
    public static final FoodProperties BAKED_CAVE_CARROT = new FoodProperties.Builder().nutrition(5).saturationModifier(0.45f).build();
    public static final FoodProperties BAT_WING = new FoodProperties.Builder().nutrition(1).saturationModifier(0.1f).effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 600, 0), 0.4F).fast().build();
    public static final FoodProperties SMOKED_BAT_WING = new FoodProperties.Builder().nutrition(3).saturationModifier(0.15f).fast().build();
    public static final FoodProperties SILVERFISH_EGGS = new FoodProperties.Builder().nutrition(1).saturationModifier(0.05f).build();
    public static final FoodProperties WEIRD_CAVIAR = new FoodProperties.Builder().nutrition(5).saturationModifier(0.15f).build();
    public static final FoodProperties SQUID = new FoodProperties.Builder().nutrition(3).saturationModifier(0.15f).effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 600, 0), 0.4F).build();
    public static final FoodProperties GLOW_SQUID = new FoodProperties.Builder().nutrition(3).saturationModifier(0.15f).effect(() -> new MobEffectInstance(MobEffects.GLOWING, 600, 0), 1f).build();
    public static final FoodProperties BAKED_SQUID = new FoodProperties.Builder().nutrition(6).saturationModifier(0.4f).build();
    public static final FoodProperties TENTACLES = new FoodProperties.Builder().nutrition(1).saturationModifier(0.05f).build();
    public static final FoodProperties BAKED_TENTACLES = new FoodProperties.Builder().nutrition(2).saturationModifier(0.1f).fast().build();
    public static final FoodProperties IMPROVISED_BARBECUE_STICK = new FoodProperties.Builder().nutrition(7).saturationModifier(0.7f).fast().build();
    public static final FoodProperties PASTA_WITH_VEGGIEBALLS = new FoodProperties.Builder().nutrition(12).saturationModifier(1f).effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 3600, 0), 1.0F).build();
    public static final FoodProperties CAVE_SOUP = new FoodProperties.Builder().nutrition(10).saturationModifier(0.4f).effect(() -> new MobEffectInstance(ModEffects.COMFORT, 3600, 0), 1.0F).build();
    public static final FoodProperties BOWL_OF_STUFFED_SQUID = new FoodProperties.Builder().nutrition(10).saturationModifier(0.8f).effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, 6000, 0), 1.0F).build();
    public static final FoodProperties HOT_COCOA = new FoodProperties.Builder().effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 1800), 1).alwaysEdible().build();

    //Not really sure how to get the values for these with the comfort effect included
    public static final FoodProperties BEETROOT_SOUP = new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).effect(() -> new MobEffectInstance(ModEffects.COMFORT, 3600, 0), 1.0F).build();
    public static final FoodProperties MUSHROOM_STEW = new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).effect(() -> new MobEffectInstance(ModEffects.COMFORT, 3600, 0), 1.0F).build();
    public static final FoodProperties RABBIT_STEW = new FoodProperties.Builder().nutrition(10).saturationModifier(0.6F).effect(() -> new MobEffectInstance(ModEffects.COMFORT, 6000, 0), 1.0F).build();

    public static FoodProperties copyAndAddHaste(FoodProperties foodProperties) {
        var builder = new FoodProperties.Builder().nutrition(foodProperties.nutrition()).saturationModifier(foodProperties.saturation());
        for (FoodProperties.PossibleEffect possibleEffect : foodProperties.effects()) {
            builder.effect(possibleEffect.effectSupplier(), possibleEffect.probability());
        }
        if (foodProperties.eatSeconds() < 1) {
            builder.fast();
        }
        if (foodProperties.canAlwaysEat()) {
            builder.alwaysEdible();
        }
        builder.effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 1800), 1);
        return builder.build();
    }
}