package com.sammy.minersdelight.data;

import com.sammy.minersdelight.MinersDelightMod;
import com.sammy.minersdelight.setup.MDBlocks;
import com.sammy.minersdelight.setup.MDItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;
import static net.minecraft.data.recipes.ShapelessRecipeBuilder.shapeless;
import static net.minecraft.data.recipes.SimpleCookingRecipeBuilder.smelting;
import static net.minecraft.data.recipes.SimpleCookingRecipeBuilder.smoking;

public class MDRecipeProvider extends RecipeProvider {
    public MDRecipeProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    public String getName() {
        return "Miner's Delight Recipe Provider";
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        shaped(MDBlocks.COPPER_POT.get())
                .define('X', Tags.Items.INGOTS_COPPER)
                .define('Y', Items.WOODEN_SHOVEL)
                .define('Z', Ingredient.of(Items.WATER_BUCKET, MDItems.WATER_CUP.get()))
                .pattern(" Y ").pattern("XZX").pattern("XXX")
                .unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER))
                .save(consumer, MinersDelightMod.path("copper_pot"));

        shaped(MDItems.COPPER_CUP.get())
                .define('X', Tags.Items.INGOTS_COPPER)
                .pattern("X X").pattern(" X ")
                .unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER))
                .save(consumer, MinersDelightMod.path("copper_cup"));

        shaped(MDBlocks.CAVE_CARROT_CRATE.get())
                .define('X', MDItems.CAVE_CARROT.get())
                .pattern("XXX").pattern("XXX").pattern("XXX")
                .unlockedBy("has_cave_carrot", has(MDItems.CAVE_CARROT.get()))
                .save(consumer, MinersDelightMod.path("cave_carrot_crate"));

        shapeless(MDItems.IMPROVISED_BARBECUE_STICK.get(), 2)
                .requires(Tags.Items.RODS_WOODEN)
                .requires(Tags.Items.RODS_WOODEN)
                .requires(MDItems.BAKED_CAVE_CARROT.get())
                .requires(Items.BROWN_MUSHROOM)
                .requires(MDItems.SMOKED_BAT_WING.get())
                .requires(MDItems.SMOKED_BAT_WING.get())
                .unlockedBy("has_cave_carrot", has(MDItems.CAVE_CARROT.get()))
                .save(consumer, MinersDelightMod.path("improvised_barbecue_stick"));

        shapeless(MDItems.CAVE_CARROT.get(), 9)
                .requires(MDBlocks.CAVE_CARROT_CRATE.get())
                .unlockedBy("has_cave_carrot", has(MDItems.CAVE_CARROT.get()))
                .save(consumer, MinersDelightMod.path("cave_carrots_from_crate"));

        smelting(Ingredient.of(MDItems.CAVE_CARROT.get()), MDItems.BAKED_CAVE_CARROT.get(), 0.25f, 200)
                .unlockedBy("has_cave_carrot", has(MDItems.CAVE_CARROT.get()))
                .save(consumer, "baked_cave_carrot");
        smoking(Ingredient.of(MDItems.CAVE_CARROT.get()), MDItems.BAKED_CAVE_CARROT.get(), 0.25f, 100)
                .unlockedBy("has_cave_carrot", has(MDItems.CAVE_CARROT.get()))
                .save(consumer, "baked_cave_carrot_smoking");

        smelting(Ingredient.of(MDItems.BAT_WING.get()), MDItems.SMOKED_BAT_WING.get(), 0.25f, 200)
                .unlockedBy("has_bat_wing", has(MDItems.BAT_WING.get()))
                .save(consumer, "smoked_bat_wing");
        smoking(Ingredient.of(MDItems.BAT_WING.get()), MDItems.SMOKED_BAT_WING.get(), 0.25f, 100)
                .unlockedBy("has_bat_wing", has(MDItems.BAT_WING.get()))
                .save(consumer, "smoked_bat_wing_smoking");

        smelting(Ingredient.of(MDItems.TENTACLES.get()), MDItems.BAKED_TENTACLES.get(), 0.25f, 200)
                .unlockedBy("has_tentacles", has(MDItems.TENTACLES.get()))
                .save(consumer, "baked_tentacles");
        smoking(Ingredient.of(MDItems.TENTACLES.get()), MDItems.BAKED_TENTACLES.get(), 0.25f, 100)
                .unlockedBy("has_tentacles", has(MDItems.TENTACLES.get()))
                .save(consumer, "baked_tentacles_smoking");

        smelting(Ingredient.of(MDItems.SQUID.get(), MDItems.GLOW_SQUID.get()), MDItems.BAKED_SQUID.get(), 0.25f, 200)
                .unlockedBy("has_squid", has(MDItems.SQUID.get()))
                .save(consumer, "baked_squid");
        smoking(Ingredient.of(MDItems.SQUID.get(), MDItems.GLOW_SQUID.get()), MDItems.BAKED_SQUID.get(), 0.25f, 100)
                .unlockedBy("has_squid", has(MDItems.SQUID.get()))
                .save(consumer, "baked_squid_smoking");
    }
}
