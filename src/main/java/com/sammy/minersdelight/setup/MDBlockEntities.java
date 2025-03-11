package com.sammy.minersdelight.setup;

import com.sammy.minersdelight.*;
import com.sammy.minersdelight.content.block.copper_pot.CopperPotBlockEntity;
import com.sammy.minersdelight.content.block.sticky_basket.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.block.entity.*;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.*;

public class MDBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MinersDelightMod.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CopperPotBlockEntity>> COPPER_POT = BLOCK_ENTITIES.register("copper_pot",
            () -> BlockEntityType.Builder.of(CopperPotBlockEntity::new, MDBlocks.COPPER_POT.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StickyBasketBlockEntity>> STICKY_BASKET = BLOCK_ENTITIES.register("sticky_basket",
            () -> BlockEntityType.Builder.of(StickyBasketBlockEntity::new, MDBlocks.STICKY_BASKET.get()).build(null));

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MDBlockEntities.COPPER_POT.get(), CopperPotBlockEntity::getItemHandler);
    }
}