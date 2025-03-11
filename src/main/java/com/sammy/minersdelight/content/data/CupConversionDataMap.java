package com.sammy.minersdelight.content.data;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;

public record CupConversionDataMap(Holder<Item> cupVariant) {
    public static final Codec<CupConversionDataMap> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.ITEM_NON_AIR_CODEC.fieldOf("cup_variant").forGetter(CupConversionDataMap::cupVariant)
    ).apply(instance, CupConversionDataMap::new));
}