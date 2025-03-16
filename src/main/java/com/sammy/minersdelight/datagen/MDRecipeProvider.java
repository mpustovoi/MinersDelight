package com.sammy.minersdelight.datagen;

import com.sammy.minersdelight.*;
import com.sammy.minersdelight.setup.*;
import net.minecraft.core.*;
import net.minecraft.data.*;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.common.*;
import vectorwing.farmersdelight.client.recipebook.*;
import vectorwing.farmersdelight.common.registry.*;
import vectorwing.farmersdelight.data.builder.*;

import java.util.concurrent.*;

import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;
import static net.minecraft.data.recipes.ShapelessRecipeBuilder.shapeless;
import static net.minecraft.data.recipes.SimpleCookingRecipeBuilder.*;
import static vectorwing.farmersdelight.data.recipe.CookingRecipes.*;

public class MDRecipeProvider extends RecipeProvider {

    public MDRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public String getName() {
        return "Miner's Delight Recipe Provider";
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        shaped(RecipeCategory.FOOD, MDItems.COPPER_POT.get())
                .define('X', Tags.Items.INGOTS_COPPER)
                .define('Y', Items.WOODEN_SHOVEL)
                .define('Z', Ingredient.of(Items.WATER_BUCKET, MDItems.WATER_CUP.get()))
                .pattern(" Y ").pattern("XZX").pattern("XXX")
                .unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER))
                .save(recipeOutput, MinersDelightMod.path("copper_pot"));

        shaped(RecipeCategory.MISC, MDItems.STICKY_BASKET.get())
                .define('X', Tags.Items.RODS_WOODEN)
                .define('Y', Items.COBWEB)
                .pattern("X X").pattern("Y Y").pattern("XYX")
                .unlockedBy("has_cobweb", has(Items.COBWEB))
                .save(recipeOutput, MinersDelightMod.path("sticky_basket"));

        shaped(RecipeCategory.TOOLS, MDItems.COPPER_CUP.get())
                .define('X', Tags.Items.INGOTS_COPPER)
                .pattern("X X").pattern(" X ")
                .unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER))
                .save(recipeOutput, MinersDelightMod.path("copper_cup"));

        shaped(RecipeCategory.BUILDING_BLOCKS, MDBlocks.CAVE_CARROT_CRATE.get())
                .define('X', MDItems.CAVE_CARROT.get())
                .pattern("XXX").pattern("XXX").pattern("XXX")
                .unlockedBy("has_cave_carrot", has(MDItems.CAVE_CARROT.get()))
                .save(recipeOutput, MinersDelightMod.path("cave_carrot_crate"));

        shapeless(RecipeCategory.FOOD, MDItems.IMPROVISED_BARBECUE_STICK.get(), 2)
                .requires(Tags.Items.RODS_WOODEN)
                .requires(Tags.Items.RODS_WOODEN)
                .requires(MDItems.BAKED_CAVE_CARROT.get())
                .requires(Items.BROWN_MUSHROOM)
                .requires(MDItems.SMOKED_BAT_WING.get())
                .requires(MDItems.SMOKED_BAT_WING.get())
                .unlockedBy("has_cave_carrot", has(MDItems.CAVE_CARROT.get()))
                .save(recipeOutput, MinersDelightMod.path("improvised_barbecue_stick"));

        shapeless(RecipeCategory.FOOD, MDItems.WEIRD_CAVIAR.get(), 1)
                .requires(Items.BOWL)
                .requires(MDItems.SILVERFISH_EGGS.get(), 3)
                .unlockedBy("has_silverfish_eggs", has(MDItems.SILVERFISH_EGGS.get()))
                .save(recipeOutput, MinersDelightMod.path("weird_caviar"));

        shapeless(RecipeCategory.FOOD, MDItems.CAVE_CARROT.get(), 9)
                .requires(MDBlocks.CAVE_CARROT_CRATE.get())
                .unlockedBy("has_cave_carrot", has(MDItems.CAVE_CARROT.get()))
                .save(recipeOutput, MinersDelightMod.path("cave_carrots_from_crate"));

        shapeless(RecipeCategory.MISC, Items.STRING, 2)
                .requires(MDBlocks.GOSSYPIUM.get())
                .unlockedBy("has_gossypium", has(MDBlocks.GOSSYPIUM.get()))
                .save(recipeOutput, MinersDelightMod.path("string_from_gossypium"));

        smelting(Ingredient.of(MDItems.CAVE_CARROT.get()), RecipeCategory.FOOD, MDItems.BAKED_CAVE_CARROT.get(), 0.25f, 200)
                .unlockedBy("has_cave_carrot", has(MDItems.CAVE_CARROT.get()))
                .save(recipeOutput, "baked_cave_carrot");
        smoking(Ingredient.of(MDItems.CAVE_CARROT.get()), RecipeCategory.FOOD, MDItems.BAKED_CAVE_CARROT.get(), 0.25f, 100)
                .unlockedBy("has_cave_carrot", has(MDItems.CAVE_CARROT.get()))
                .save(recipeOutput, "baked_cave_carrot_smoking");
        campfireCooking(Ingredient.of(MDItems.CAVE_CARROT.get()), RecipeCategory.FOOD, MDItems.BAKED_CAVE_CARROT.get(), 0.25f, 600)
                .unlockedBy("has_cave_carrot", has(MDItems.CAVE_CARROT.get()))
                .save(recipeOutput, "baked_cave_carrot_campfire");

        smelting(Ingredient.of(MDItems.BAT_WING.get()), RecipeCategory.FOOD, MDItems.SMOKED_BAT_WING.get(), 0.25f, 200)
                .unlockedBy("has_bat_wing", has(MDItems.BAT_WING.get()))
                .save(recipeOutput, "smoked_bat_wing");
        smoking(Ingredient.of(MDItems.BAT_WING.get()), RecipeCategory.FOOD, MDItems.SMOKED_BAT_WING.get(), 0.25f, 100)
                .unlockedBy("has_bat_wing", has(MDItems.BAT_WING.get()))
                .save(recipeOutput, "smoked_bat_wing_smoking");
        campfireCooking(Ingredient.of(MDItems.BAT_WING.get()), RecipeCategory.FOOD, MDItems.SMOKED_BAT_WING.get(), 0.25f, 600)
                .unlockedBy("has_bat_wing", has(MDItems.BAT_WING.get()))
                .save(recipeOutput, "smoked_bat_wing_campfire");

        smelting(Ingredient.of(MDItems.TENTACLES.get()), RecipeCategory.FOOD, MDItems.BAKED_TENTACLES.get(), 0.25f, 200)
                .unlockedBy("has_tentacles", has(MDItems.TENTACLES.get()))
                .save(recipeOutput, "baked_tentacles");
        smoking(Ingredient.of(MDItems.TENTACLES.get()), RecipeCategory.FOOD, MDItems.BAKED_TENTACLES.get(), 0.25f, 100)
                .unlockedBy("has_tentacles", has(MDItems.TENTACLES.get()))
                .save(recipeOutput, "baked_tentacles_smoking");
        campfireCooking(Ingredient.of(MDItems.TENTACLES.get()), RecipeCategory.FOOD, MDItems.BAKED_TENTACLES.get(), 0.25f, 600)
                .unlockedBy("has_tentacles", has(MDItems.TENTACLES.get()))
                .save(recipeOutput, "baked_tentacles_campfire");

        smelting(Ingredient.of(MDItems.SQUID.get(), MDItems.GLOW_SQUID.get()), RecipeCategory.FOOD, MDItems.BAKED_SQUID.get(), 0.25f, 200)
                .unlockedBy("has_squid", has(MDItems.SQUID.get()))
                .save(recipeOutput, "baked_squid");
        smoking(Ingredient.of(MDItems.SQUID.get(), MDItems.GLOW_SQUID.get()), RecipeCategory.FOOD, MDItems.BAKED_SQUID.get(), 0.25f, 100)
                .unlockedBy("has_squid", has(MDItems.SQUID.get()))
                .save(recipeOutput, "baked_squid_smoking");
        campfireCooking(Ingredient.of(MDItems.SQUID.get(), MDItems.GLOW_SQUID.get()), RecipeCategory.FOOD, MDItems.BAKED_SQUID.get(), 0.25f, 600)
                .unlockedBy("has_squid", has(MDItems.SQUID.get()))
                .save(recipeOutput, "baked_squid_campfire");

        CookingPotRecipeBuilder.cookingPotRecipe(MDItems.PASTA_WITH_VEGGIEBALLS.get(), 1, SLOW_COOKING, LARGE_EXP)
                .addIngredient(MDItems.CAVE_CARROT.get())
                .addIngredient(ModItems.RAW_PASTA.get())
                .addIngredient(ModItems.TOMATO_SAUCE.get())
                .unlockedByAnyIngredient(MDItems.CAVE_CARROT.get(), ModItems.RAW_PASTA.get(), ModItems.TOMATO_SAUCE.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .build(recipeOutput, "minersdelight:cooking/pasta_with_veggieballs");

        CookingPotRecipeBuilder.cookingPotRecipe(MDItems.CAVE_SOUP.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
                .addIngredient(MDItems.CAVE_CARROT.get())
                .addIngredient(Items.RED_MUSHROOM)
                .addIngredient(Items.BROWN_MUSHROOM, 2)
                .unlockedByAnyIngredient(MDItems.CAVE_CARROT.get(), Items.RED_MUSHROOM, Items.BROWN_MUSHROOM)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .build(recipeOutput, "minersdelight:cooking/cave_soup");

        CookingPotRecipeBuilder.cookingPotRecipe(MDBlocks.STUFFED_SQUID.get(), 1, SLOW_COOKING, LARGE_EXP, Items.BOWL)
                .addIngredient(Ingredient.of(MDItems.SQUID.get(), MDItems.GLOW_SQUID.get(), MDItems.BAKED_SQUID.get()))
                .addIngredient(ModItems.RICE.get(), 2)
                .addIngredient(Ingredient.of(MDItems.CAVE_CARROT.get(), Items.CARROT))
                .addIngredient(Tags.Items.EGGS)
                .addIngredient(ModItems.ONION.get())
                .unlockedByAnyIngredient(MDItems.SQUID.get(), MDItems.GLOW_SQUID.get(), MDItems.BAKED_SQUID.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .build(recipeOutput, "minersdelight:cooking/stuffed_squid");

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MDItems.BAT_WING.get()), Ingredient.of(MDItemTags.KNIVES_FD), Items.PHANTOM_MEMBRANE, 1)
                .addResultWithChance(Items.PHANTOM_MEMBRANE, 0.5f, 2)
                .build(recipeOutput, "minersdelight:cutting/bat_wing");

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MDItems.SQUID.get()), Ingredient.of(MDItemTags.KNIVES_FD), MDItems.TENTACLES.get(), 3)
                .addResultWithChance(MDItems.TENTACLES.get(), 0.5f)
                .addResult(Items.INK_SAC)
                .addResultWithChance(Items.INK_SAC, 0.5f, 2)
                .build(recipeOutput, "minersdelight:cutting/squid");

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MDItems.GLOW_SQUID.get()), Ingredient.of(MDItemTags.KNIVES_FD), MDItems.TENTACLES.get(), 3)
                .addResultWithChance(MDItems.TENTACLES.get(), 0.5f)
                .addResult(Items.GLOW_INK_SAC)
                .addResultWithChance(Items.GLOW_INK_SAC, 0.5f, 2)
                .build(recipeOutput, "minersdelight:cutting/glow_squid");


        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MDBlocks.WILD_CAVE_CARROTS.get()), Ingredient.of(Tags.Items.TOOLS_SHEAR), MDBlocks.GOSSYPIUM.get(), 1)
                .addResultWithChance(MDBlocks.GOSSYPIUM.get(), 0.5F, 1)
                .addResult(MDItems.CAVE_CARROT.get())
                .addResultWithChance(MDItems.CAVE_CARROT.get(), 0.5f, 2)
                .build(recipeOutput, "minersdelight:cutting/wild_cave_carrot");
    }
}