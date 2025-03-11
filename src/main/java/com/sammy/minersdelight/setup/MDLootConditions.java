package com.sammy.minersdelight.setup;

import com.sammy.minersdelight.*;
import com.sammy.minersdelight.content.loot.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.neoforged.neoforge.registries.*;

public class MDLootConditions {
	public static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS = DeferredRegister.create(BuiltInRegistries.LOOT_CONDITION_TYPE.key(), MinersDelightMod.MODID);

	public static final DeferredHolder<LootItemConditionType, LootItemConditionType> BLOCK_TAG_CONDITION = LOOT_CONDITIONS.register("block_tag", ()->new LootItemConditionType(LootItemBlockTagCondition.CODEC));
}
