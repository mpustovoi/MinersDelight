package com.sammy.minersdelight.content.block.copper_pot;

import com.google.common.collect.*;
import com.sammy.minersdelight.setup.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.core.HolderLookup.*;
import net.minecraft.core.component.DataComponentMap.*;
import net.minecraft.core.component.*;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.items.*;
import net.neoforged.neoforge.items.wrapper.*;
import vectorwing.farmersdelight.common.block.*;
import vectorwing.farmersdelight.common.block.entity.*;
import vectorwing.farmersdelight.common.crafting.*;
import vectorwing.farmersdelight.common.registry.*;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import javax.annotation.*;
import java.util.*;

public class CopperPotBlockEntity extends SyncedBlockEntity implements MenuProvider, HeatableBlockEntity, Nameable, RecipeCraftingHolder {
	public static final int MEAL_DISPLAY_SLOT = 4;
	public static final int CONTAINER_SLOT = 5;
	public static final int OUTPUT_SLOT = 6;
	public static final int INVENTORY_SIZE = OUTPUT_SLOT + 1;

	private final ItemStackHandler inventory;
	private final IItemHandler inputHandler;
	private final IItemHandler outputHandler;

	private int cookTime;
	private int cookTimeTotal;
	private ItemStack mealContainerStack;
	private Component customName;

	protected final ContainerData cookingPotData;
	private final Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;
	private final RecipeManager.CachedCheck<RecipeWrapper, CookingPotRecipe> quickCheck;

	private ResourceLocation lastRecipeID;
	private boolean checkNewRecipe;

	public CopperPotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.inventory = createHandler();
		this.inputHandler = new CopperPotItemHandler(inventory, Direction.UP);
		this.outputHandler = new CopperPotItemHandler(inventory, Direction.DOWN);
		this.mealContainerStack = ItemStack.EMPTY;
		this.cookingPotData = createIntArray();
		this.usedRecipeTracker = new Object2IntOpenHashMap<>();
		this.checkNewRecipe = true;
		quickCheck = RecipeManager.createCheck(ModRecipeTypes.COOKING.get());
	}

	public CopperPotBlockEntity(BlockPos pos, BlockState state) {
		this(MDBlockEntities.COPPER_POT.get(), pos, state);
	}

	public static ItemStack getMealFromItem(ItemStack cookingPotStack, HolderLookup.Provider registries) {
		if (!cookingPotStack.is(MDBlocks.COPPER_POT.get().asItem())) {
			return ItemStack.EMPTY;
		}

		CustomData blockEntityData = cookingPotStack.get(DataComponents.BLOCK_ENTITY_DATA);
		if (blockEntityData != null) {
			CompoundTag compound = blockEntityData.copyTag();
			CompoundTag inventoryTag = compound.getCompound("Inventory");
			if (inventoryTag.contains("Items", 9)) {
				ItemStackHandler handler = new ItemStackHandler();
				handler.deserializeNBT(registries, inventoryTag);
				return handler.getStackInSlot(4);
			}
		}

		return ItemStack.EMPTY;
	}

	public static void takeServingFromItem(ItemStack cookingPotStack, HolderLookup.Provider registries) {
		if (!cookingPotStack.is(MDBlocks.COPPER_POT.get().asItem())) {
			return;
		}

		CustomData blockEntityData = cookingPotStack.get(DataComponents.BLOCK_ENTITY_DATA);
		if (blockEntityData != null) {
			CompoundTag compound = blockEntityData.copyTag();
			CompoundTag inventoryTag = compound.getCompound("Inventory");
			if (inventoryTag.contains("Items", 9)) {
				ItemStackHandler handler = new ItemStackHandler();
				handler.deserializeNBT(registries, inventoryTag);
				ItemStack newMealStack = handler.getStackInSlot(4);
				newMealStack.shrink(1);
				compound.remove("Inventory");
				compound.put("Inventory", handler.serializeNBT(registries));
			}
		}
	}

	public static ItemStack getContainerFromItem(ItemStack cookingPotStack, HolderLookup.Provider registries) {
		if (!cookingPotStack.is(MDBlocks.COPPER_POT.get().asItem())) {
			return ItemStack.EMPTY;
		}

		CustomData blockEntityData = cookingPotStack.get(DataComponents.BLOCK_ENTITY_DATA);
		if (blockEntityData != null) {
			CompoundTag compound = blockEntityData.copyTag();
			return ItemStack.parseOptional(registries, compound.getCompound("Container"));
		}

		return ItemStack.EMPTY;
	}

	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.loadAdditional(compound, registries);
		inventory.deserializeNBT(registries, compound.getCompound("Inventory"));
		cookTime = compound.getInt("CookTime");
		cookTimeTotal = compound.getInt("CookTimeTotal");
		mealContainerStack = ItemStack.parseOptional(registries, compound.getCompound("Container"));
		if (compound.contains("CustomName", 8)) {
			customName = Component.Serializer.fromJson(compound.getString("CustomName"), registries);
		}
		CompoundTag compoundRecipes = compound.getCompound("RecipesUsed");
		for (String key : compoundRecipes.getAllKeys()) {
			usedRecipeTracker.put(ResourceLocation.parse(key), compoundRecipes.getInt(key));
		}
	}

	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.saveAdditional(compound, registries);
		compound.putInt("CookTime", cookTime);
		compound.putInt("CookTimeTotal", cookTimeTotal);
		compound.put("Container", mealContainerStack.saveOptional(registries));
		if (customName != null) {
			compound.putString("CustomName", Component.Serializer.toJson(customName, registries));
		}
		compound.put("Inventory", inventory.serializeNBT(registries));
		CompoundTag compoundRecipes = new CompoundTag();
		usedRecipeTracker.forEach((recipeId, craftedAmount) -> compoundRecipes.putInt(recipeId.toString(), craftedAmount));
		compound.put("RecipesUsed", compoundRecipes);
	}

	private CompoundTag writeItems(CompoundTag compound, HolderLookup.Provider registries) {
		super.saveAdditional(compound, registries);
		compound.put("Container", mealContainerStack.saveOptional(registries));
		compound.put("Inventory", inventory.serializeNBT(registries));
		return compound;
	}

	public CompoundTag writeMeal(CompoundTag compound, HolderLookup.Provider registries) {
		if (getMeal().isEmpty()) return compound;

		ItemStackHandler drops = new ItemStackHandler(INVENTORY_SIZE);
		for (int i = 0; i < INVENTORY_SIZE; ++i) {
			drops.setStackInSlot(i, i == MEAL_DISPLAY_SLOT ? inventory.getStackInSlot(i) : ItemStack.EMPTY);
		}
		if (customName != null) {
			compound.putString("CustomName", Component.Serializer.toJson(customName, registries));
		}
		compound.put("Container", mealContainerStack.saveOptional(registries));
		compound.put("Inventory", drops.serializeNBT(registries));
		return compound;
	}

	public static void cookingTick(Level level, BlockPos pos, BlockState state, CopperPotBlockEntity cookingPot) {
		boolean isHeated = cookingPot.isHeated(level, pos);
		boolean didInventoryChange = false;

		if (isHeated && cookingPot.hasInput()) {
			Optional<RecipeHolder<CookingPotRecipe>> optionalRecipeHolder = cookingPot.getMatchingRecipe(cookingPot.createFakeRecipeWrapper());
			if (optionalRecipeHolder.isPresent() && cookingPot.canCook(optionalRecipeHolder.get().value())) {
				didInventoryChange = cookingPot.processCooking(optionalRecipeHolder.get(), cookingPot);
			} else {
				cookingPot.cookTime = 0;
			}
		} else if (cookingPot.cookTime > 0) {
			cookingPot.cookTime = Mth.clamp(cookingPot.cookTime - 2, 0, cookingPot.cookTimeTotal);
		}

		ItemStack mealStack = cookingPot.getMeal();
		if (!mealStack.isEmpty()) {
			if (!cookingPot.doesMealHaveContainer(mealStack)) {
				cookingPot.moveMealToOutput();
				didInventoryChange = true;
			} else if (!cookingPot.inventory.getStackInSlot(CONTAINER_SLOT).isEmpty()) {
				cookingPot.useStoredContainersOnMeal();
				didInventoryChange = true;
			}
		}

		if (didInventoryChange) {
			cookingPot.inventoryChanged();
		}
	}


	public static void animationTick(Level level, BlockPos pos, BlockState state, CopperPotBlockEntity cookingPot) {
		if (cookingPot.isHeated(level, pos)) {
			RandomSource random = level.random;
			if (random.nextFloat() < 0.2F) {
				double x = (double) pos.getX() + 0.5D + (random.nextDouble() * 0.6D - 0.3D);
				double y = (double) pos.getY() + 0.7D;
				double z = (double) pos.getZ() + 0.5D + (random.nextDouble() * 0.6D - 0.3D);
				level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0D, 0.0D, 0.0D);
			}
			if (random.nextFloat() < 0.05F) {
				double x = (double) pos.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double y = (double) pos.getY() + 0.5D;
				double z = (double) pos.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double motionY = random.nextBoolean() ? 0.015D : 0.005D;
				level.addParticle(ModParticleTypes.STEAM.get(), x, y, z, 0.0D, motionY, 0.0D);
			}
		}
	}

	//Farmer's Delight Recipes compare items in the inventory through slots 0 to 5, normally these slots are all cooking ingredients,
	//but in our case slot 4 & 5 is the bowl and output, so we need to shift them back.
	public RecipeWrapper createFakeRecipeWrapper() {
		ItemStackHandler handler = new ItemStackHandler(9);
		for (int i = 0; i < 7; i++) {
			int slot = i;
			if (i >= 4) {
				slot += 2;
			}
			handler.setStackInSlot(slot, inventory.getStackInSlot(i));
		}
		return new RecipeWrapper(handler);
	}

	private Optional<RecipeHolder<CookingPotRecipe>> getMatchingRecipe(RecipeWrapper inventoryWrapper) {
		if (level == null) return Optional.empty();

		if (lastRecipeID != null) {
			Optional<RecipeHolder<CookingPotRecipe>> optionalRecipeHolder =  quickCheck.getRecipeFor(inventoryWrapper, level);
//			Recipe<RecipeWrapper> recipe = ((RecipeManagerAccessor) level.getRecipeManager())
//					.getRecipeMap(ModRecipeTypes.COOKING.get())
//					.get(lastRecipeID);
			if (optionalRecipeHolder.isPresent()) {
				CookingPotRecipe recipe = optionalRecipeHolder.get().value();
				if (recipe.matches(inventoryWrapper, level)) {
					return optionalRecipeHolder;
				}
				if (ItemStack.isSameItemSameComponents(recipe.getResultItem(level.registryAccess()), getMeal())) {
					return Optional.empty();
				}
			}
		}

		return Optional.empty();
	}

	public ItemStack getContainer() {
		if (!mealContainerStack.isEmpty()) {
			return mealContainerStack;
		} else {
			return getMeal().getCraftingRemainingItem();
		}
	}

	private boolean hasInput() {
		for (int i = 0; i < MEAL_DISPLAY_SLOT; ++i) {
			if (!inventory.getStackInSlot(i).isEmpty()) return true;
		}
		return false;
	}

	protected boolean canCook(CookingPotRecipe recipe) {
		if (hasInput()) {
			ItemStack resultStack = recipe.getResultItem(level.registryAccess());
			var data = resultStack.getItem().builtInRegistryHolder().getData(MDDataMaps.CUP_VARIANT);
			if (data != null) {
				ItemStack cupResultStack = new ItemStack(data.cupVariant(), resultStack.getCount());
				cupResultStack.applyComponents(resultStack.getComponents());
				resultStack = cupResultStack;
			}
			if (resultStack.isEmpty()) {
				return false;
			} else {
				ItemStack storedMealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
				if (storedMealStack.isEmpty()) {
					return true;
				} else if (!ItemStack.isSameItemSameComponents(storedMealStack, resultStack)) {
					return false;
				} else if (storedMealStack.getCount() + resultStack.getCount() <= inventory.getSlotLimit(MEAL_DISPLAY_SLOT)) {
					return true;
				} else {
					return storedMealStack.getCount() + resultStack.getCount() <= resultStack.getMaxStackSize();
				}
			}
		} else {
			return false;
		}
	}

	private boolean processCooking(RecipeHolder<CookingPotRecipe> recipeHolder, CopperPotBlockEntity cookingPot) {
		if (level == null) return false;

		CookingPotRecipe recipe = recipeHolder.value();
		++cookTime;
		cookTimeTotal = (int) (recipe.getCookTime()*0.8f);
		if (cookTime < cookTimeTotal) {
			return false;
		}

		cookTime = 0;
		ItemStack resultStack = recipe.getResultItem(level.registryAccess());
		var data = resultStack.getItem().builtInRegistryHolder().getData(MDDataMaps.CUP_VARIANT);
		boolean cupServed = data != null;
		mealContainerStack = cupServed ? MDItems.COPPER_CUP.get().getDefaultInstance() : recipe.getOutputContainer();
		if (cupServed) {
			ItemStack cupResultStack = new ItemStack(data.cupVariant(), resultStack.getCount());
			cupResultStack.applyComponents(resultStack.getComponents());
			resultStack = cupResultStack;
		}
		ItemStack storedMealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
		if (storedMealStack.isEmpty()) {
			inventory.setStackInSlot(MEAL_DISPLAY_SLOT, resultStack.copy());
		} else if (ItemStack.isSameItemSameComponents(storedMealStack, resultStack)) {
			storedMealStack.grow(resultStack.getCount());
		}
		cookingPot.setRecipeUsed(recipeHolder);

		for (int i = 0; i < MEAL_DISPLAY_SLOT; ++i) {
			ItemStack slotStack = inventory.getStackInSlot(i);
			if (slotStack.hasCraftingRemainingItem()) {
				Direction direction = getBlockState().getValue(CookingPotBlock.FACING).getCounterClockWise();
				double x = worldPosition.getX() + 0.5 + (direction.getStepX() * 0.25);
				double y = worldPosition.getY() + 0.7;
				double z = worldPosition.getZ() + 0.5 + (direction.getStepZ() * 0.25);
				ItemUtils.spawnItemEntity(level, inventory.getStackInSlot(i).getCraftingRemainingItem(), x, y, z,
						direction.getStepX() * 0.08F, 0.25F, direction.getStepZ() * 0.08F);
			}
			if (!slotStack.isEmpty())
				slotStack.shrink(1);
		}
		return true;
	}

	@Override
	public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
		if (recipe != null) {
			ResourceLocation recipeID = recipe.id();
			usedRecipeTracker.addTo(recipeID, 1);
		}
	}

	@Nullable
	@Override
	public RecipeHolder<?> getRecipeUsed() {
		return null;
	}

	@Override
	public void awardUsedRecipes(Player player, List<ItemStack> items) {
		List<RecipeHolder<?>> usedRecipes = getUsedRecipesAndPopExperience(player.level(), player.position());
		player.awardRecipes(usedRecipes);
		usedRecipeTracker.clear();
	}

	public List<RecipeHolder<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
		List<RecipeHolder<?>> list = Lists.newArrayList();

		for (Object2IntMap.Entry<ResourceLocation> entry : usedRecipeTracker.object2IntEntrySet()) {
			level.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
				list.add(recipe);
				splitAndSpawnExperience((ServerLevel) level, pos, entry.getIntValue(), ((CookingPotRecipe) recipe.value()).getExperience());
			});
		}

		return list;
	}

	private static void splitAndSpawnExperience(ServerLevel level, Vec3 pos, int craftedAmount, float experience) {
		int expTotal = Mth.floor((float) craftedAmount * experience);
		float expFraction = Mth.frac((float) craftedAmount * experience);
		if (expFraction != 0.0F && Math.random() < (double) expFraction) {
			++expTotal;
		}

		ExperienceOrb.award(level, pos, expTotal);
	}

	public boolean isHeated() {
		if (level == null) return false;
		return this.isHeated(level, worldPosition);
	}

	public ItemStackHandler getInventory() {
		return inventory;
	}

	public ItemStack getMeal() {
		return inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
	}

	public NonNullList<ItemStack> getDroppableInventory() {
		NonNullList<ItemStack> drops = NonNullList.create();
		for (int i = 0; i < INVENTORY_SIZE; ++i) {
			if (i != MEAL_DISPLAY_SLOT) {
				drops.add(inventory.getStackInSlot(i));
			}
		}
		return drops;
	}

	private void moveMealToOutput() {
		ItemStack mealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
		ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);
		int mealCount = Math.min(mealStack.getCount(), mealStack.getMaxStackSize() - outputStack.getCount());
		if (outputStack.isEmpty()) {
			inventory.setStackInSlot(OUTPUT_SLOT, mealStack.split(mealCount));
		} else if (outputStack.getItem() == mealStack.getItem()) {
			mealStack.shrink(mealCount);
			outputStack.grow(mealCount);
		}
	}

	private void useStoredContainersOnMeal() {
		ItemStack mealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
		ItemStack containerInputStack = inventory.getStackInSlot(CONTAINER_SLOT);
		ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);

		if (isContainerValid(containerInputStack) && outputStack.getCount() < outputStack.getMaxStackSize()) {
			int smallerStackCount = Math.min(mealStack.getCount(), containerInputStack.getCount());
			int mealCount = Math.min(smallerStackCount, mealStack.getMaxStackSize() - outputStack.getCount());
			if (outputStack.isEmpty()) {
				containerInputStack.shrink(mealCount);
				inventory.setStackInSlot(OUTPUT_SLOT, mealStack.split(mealCount));
			} else if (outputStack.getItem() == mealStack.getItem()) {
				mealStack.shrink(mealCount);
				containerInputStack.shrink(mealCount);
				outputStack.grow(mealCount);
			}
		}
	}

	public ItemStack useHeldItemOnMeal(ItemStack container) {
		if (isContainerValid(container) && !getMeal().isEmpty()) {
			container.shrink(1);
			return getMeal().split(1);
		}
		return ItemStack.EMPTY;
	}

	private boolean doesMealHaveContainer(ItemStack meal) {
		return !mealContainerStack.isEmpty() || meal.hasCraftingRemainingItem();
	}

	public boolean isContainerValid(ItemStack containerItem) {
		if (containerItem.isEmpty()) return false;
		if (!mealContainerStack.isEmpty()) {
			return ItemStack.isSameItemSameComponents(mealContainerStack, containerItem);
		} else {
			return ItemStack.isSameItemSameComponents(getMeal().getCraftingRemainingItem(), containerItem);
		}
	}

	@Override
	public Component getName() {
		return customName != null ? customName : Component.translatable("minersdelight.container.cooking_pot");
	}

	@Override
	public Component getDisplayName() {
		return getName();
	}

	@Override
	@Nullable
	public Component getCustomName() {
		return customName;
	}

	public void setCustomName(Component name) {
		customName = name;
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory player, Player entity) {
		return new CopperPotMenu(id, player, this, cookingPotData);
	}

	public IItemHandler getItemHandler(@Nullable Direction side) {
		if (side == null || side.equals(Direction.UP)) {
			return inputHandler;
		} else {
			return outputHandler;
		}
	}

	@Override
	public void saveToItem(ItemStack stack, Provider registries) {
		super.saveToItem(stack, registries);
	}

	@Override
	protected void applyImplicitComponents(DataComponentInput input) {
		super.applyImplicitComponents(input);
		this.setCustomName(input.get(DataComponents.CUSTOM_NAME));
	}

	@Override
	protected void collectImplicitComponents(Builder builder) {
		super.collectImplicitComponents(builder);
		builder.set(DataComponents.CUSTOM_NAME, this.getName());
	}

	@Override
	public void removeComponentsFromTag(CompoundTag tag) {
		super.removeComponentsFromTag(tag);
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
//		inputHandler.invalidate();
//		outputHandler.invalidate();
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return writeItems(new CompoundTag(), registries);
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler(INVENTORY_SIZE) {
			@Override
			protected void onContentsChanged(int slot) {
				if (slot >= 0 && slot < MEAL_DISPLAY_SLOT) {
					checkNewRecipe = true;
				}
				inventoryChanged();
			}
		};
	}

	private ContainerData createIntArray() {
		return new ContainerData() {
			@Override
			public int get(int index) {
				return switch (index) {
					case 0 -> CopperPotBlockEntity.this.cookTime;
					case 1 -> CopperPotBlockEntity.this.cookTimeTotal;
					default -> 0;
				};
			}

			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0 -> CopperPotBlockEntity.this.cookTime = value;
					case 1 -> CopperPotBlockEntity.this.cookTimeTotal = value;
				}
			}

			@Override
			public int getCount() {
				return 2;
			}
		};
	}
}