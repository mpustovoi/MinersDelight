package com.sammy.minersdelight.content.loot;

import com.google.common.base.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.registries.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.neoforged.neoforge.common.loot.*;

import javax.annotation.*;
import java.util.function.Supplier;

public class ModifyDroppedItemsModifier extends LootModifier {
	public static final Supplier<MapCodec<ModifyDroppedItemsModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec((inst) -> codecStart(inst)
			.and(inst.group(
					Codec.BOOL.optionalFieldOf("removeOriginalLoot", false).forGetter((m) -> m.removeOriginalLoot),
					BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter((m) -> m.item),
					Codec.INT.optionalFieldOf("min", 0).forGetter((m) -> m.min),
					Codec.INT.optionalFieldOf("max", 1).forGetter((m) -> m.max)))
			.apply(inst, ModifyDroppedItemsModifier::new)));

	private final boolean removeOriginalLoot;
	private final Item item;
	private final int min;
	private final int max;

	protected ModifyDroppedItemsModifier(LootItemCondition[] conditionsIn, boolean removeOriginalLoot, Item item, int min, int max) {
		super(conditionsIn);
        this.removeOriginalLoot = removeOriginalLoot;
        this.item = item;
		this.min = min;
		this.max = max;
	}

	@Nonnull
	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (removeOriginalLoot) {
			generatedLoot.clear();
		}
		ItemStack addedStack = new ItemStack(item, Mth.nextInt(context.getRandom(), min, max));
		if (addedStack.getCount() < addedStack.getMaxStackSize()) {
			generatedLoot.add(addedStack);
		} else {
			int i = addedStack.getCount();

			while (i > 0) {
				ItemStack subStack = addedStack.copy();
				subStack.setCount(Math.min(addedStack.getMaxStackSize(), i));
				i -= subStack.getCount();
				generatedLoot.add(subStack);
			}
		}

		return generatedLoot;
	}

	@Override
	public MapCodec<? extends net.neoforged.neoforge.common.loot.IGlobalLootModifier> codec() {
		return CODEC.get();
	}
}