package com.sammy.minersdelight.jei;

import com.sammy.minersdelight.*;
import com.sammy.minersdelight.setup.*;
import mezz.jei.api.constants.*;
import mezz.jei.api.gui.builder.*;
import mezz.jei.api.gui.drawable.*;
import mezz.jei.api.gui.ingredient.*;
import mezz.jei.api.helpers.*;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.*;
import mezz.jei.api.recipe.category.*;
import net.minecraft.*;
import net.minecraft.client.gui.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import vectorwing.farmersdelight.common.crafting.*;
import vectorwing.farmersdelight.common.utility.*;
import vectorwing.farmersdelight.integration.jei.*;

import javax.annotation.*;
import java.util.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopperPotCookingRecipeCategory implements IRecipeCategory<CookingPotRecipe>
{
	public static final RecipeType<CookingPotRecipe> COOKING = RecipeType.create(MinersDelightMod.MODID, "cooking", CookingPotRecipe.class);

	protected final IDrawable heatIndicator;
	protected final IDrawable timeIcon;
	protected final IDrawable expIcon;
	protected final IDrawableAnimated arrow;
	private final Component title;
	private final IDrawable background;
	private final IDrawable icon;

	public CopperPotCookingRecipeCategory(IGuiHelper helper) {
		title = TextUtils.getTranslation("jei.cooking");
		ResourceLocation backgroundImage = MinersDelightMod.path("textures/gui/copper_pot.png");
		background = helper.createDrawable(backgroundImage, 29, 16, 116, 56);
		icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MDItems.COPPER_POT.get()));
		heatIndicator = helper.createDrawable(backgroundImage, 176, 0, 17, 15);
		timeIcon = helper.createDrawable(backgroundImage, 176, 32, 8, 11);
		expIcon = helper.createDrawable(backgroundImage, 176, 43, 9, 9);
		arrow = helper.drawableBuilder(backgroundImage, 176, 15, 24, 17)
				.buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public RecipeType<CookingPotRecipe> getRecipeType() {
		return FDRecipeTypes.COOKING;
	}

	@Override
	public Component getTitle() {
		return this.title;
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, CookingPotRecipe recipe, IFocusGroup focusGroup) {
		NonNullList<Ingredient> recipeIngredients = recipe.getIngredients();
		ItemStack resultStack = RecipeUtils.getResultItem(recipe);
		var data = resultStack.getItem().builtInRegistryHolder().getData(MDDataMaps.CUP_VARIANT);
		if (data != null) {
			ItemStack cupResultStack = new ItemStack(data.cupVariant(), resultStack.getCount());
			cupResultStack.applyComponents(resultStack.getComponents());
			resultStack = cupResultStack;
		}
		ItemStack containerStack = recipe.getOutputContainer();

		int borderSlotSize = 18;
		for (int row = 0; row < 2; ++row) {
			for (int column = 0; column < 3; ++column) {
				int inputIndex = row * 3 + column;
				if (inputIndex < recipeIngredients.size()) {
					builder.addSlot(RecipeIngredientRole.INPUT, (column * borderSlotSize) + 1, (row * borderSlotSize) + 1)
							.addItemStacks(Arrays.asList(recipeIngredients.get(inputIndex).getItems()));
				}
			}
		}

		builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 10).addItemStack(resultStack);

		if (!containerStack.isEmpty()) {
			builder.addSlot(RecipeIngredientRole.CATALYST, 63, 39).addItemStack(containerStack);
		}

		builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 39).addItemStack(resultStack);
	}

	@Override
	public void draw(CookingPotRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		arrow.draw(guiGraphics, 60, 9);
		heatIndicator.draw(guiGraphics, 18, 39);
		timeIcon.draw(guiGraphics, 64, 2);
		if (recipe.getExperience() > 0) {
			expIcon.draw(guiGraphics, 63, 21);
		}
	}

	@Override
	public List<Component> getTooltipStrings(CookingPotRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if (ClientRenderUtils.isCursorInsideBounds(61, 2, 22, 28, mouseX, mouseY)) {
			List<Component> tooltipStrings = new ArrayList<>();

			int cookTime = recipe.getCookTime();
			if (cookTime > 0) {
				int cookTimeSeconds = cookTime / 20;
				tooltipStrings.add(Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds));
			}
			float experience = recipe.getExperience();
			if (experience > 0) {
				tooltipStrings.add(Component.translatable("gui.jei.category.smelting.experience", experience));
			}

			return tooltipStrings;
		}
		return Collections.emptyList();
	}
}