package com.sammy.minersdelight.setup;

import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;

public class MDItemTags {

    public static final TagKey<Item> KNIVES_FD = modTag("farmersdelight:tools/knives");
    public static final TagKey<Item> KNIVES = commonTag("tools/knives");

    private static TagKey<Item> modTag(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.parse(path));
    }

    private static TagKey<Item> commonTag(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
    }
}
