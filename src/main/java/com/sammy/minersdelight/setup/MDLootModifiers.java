package com.sammy.minersdelight.setup;

import com.mojang.serialization.*;
import com.sammy.minersdelight.*;
import com.sammy.minersdelight.content.loot.*;
import net.neoforged.neoforge.common.loot.*;
import net.neoforged.neoforge.registries.*;

import java.util.function.*;

public class MDLootModifiers {

	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MinersDelightMod.MODID);
	public static final Supplier<MapCodec<? extends IGlobalLootModifier>> ADD_ITEMS = LOOT_MODIFIERS.register("add_items", ModifyDroppedItemsModifier.CODEC);
	public static final Supplier<MapCodec<? extends IGlobalLootModifier>> REPLACE_ITEMS = LOOT_MODIFIERS.register("replace_items", ReplaceDroppedLootModifier.Serializer::new);

}
