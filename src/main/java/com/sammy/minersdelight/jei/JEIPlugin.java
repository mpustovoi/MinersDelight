package com.sammy.minersdelight.jei;

import com.sammy.minersdelight.MinersDelightMod;
import com.sammy.minersdelight.content.block.copper_pot.CopperPotMenu;
import com.sammy.minersdelight.content.block.copper_pot.CopperPotScreen;
import com.sammy.minersdelight.setup.MDBlocks;
import com.sammy.minersdelight.setup.MDMenuTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@JeiPlugin
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin
{
	private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MinersDelightMod.MODID, "jei_plugin");
	private static final Minecraft MC = Minecraft.getInstance();

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new CopperPotCookingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		assert MC.level != null;
		List<CookingPotRecipe> copperPotRecipes = new ArrayList<>(MC.level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get()).stream()
				.map(RecipeHolder::value).toList());

		copperPotRecipes.removeIf(r -> r.getIngredients().stream().filter(i -> !i.isEmpty()).count() > 4);
		registration.addRecipes(CopperPotCookingRecipeCategory.COOKING, copperPotRecipes);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(MDBlocks.COPPER_POT.get()), CopperPotCookingRecipeCategory.COOKING);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(CopperPotScreen.class, 78, 28, 28, 15, CopperPotCookingRecipeCategory.COOKING);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(CopperPotMenu.class, MDMenuTypes.COPPER_POT.get(), CopperPotCookingRecipeCategory.COOKING, 0, 6, 9, 36);
	}

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}
}
