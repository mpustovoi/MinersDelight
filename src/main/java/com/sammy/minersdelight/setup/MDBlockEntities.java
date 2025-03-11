package com.sammy.minersdelight.setup;

import com.sammy.minersdelight.*;
import com.sammy.minersdelight.content.block.sticky_basket.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.block.entity.*;
import net.neoforged.neoforge.registries.*;

public class MDBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MinersDelightMod.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StickyBasketBlockEntity>> STICKY_BASKET = TILES.register("sticky_basket",
            () -> BlockEntityType.Builder.of(StickyBasketBlockEntity::new, MDBlocks.STICKY_BASKET.get()).build(null));
}