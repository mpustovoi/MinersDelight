package com.sammy.minersdelight.setup;

import com.sammy.minersdelight.*;
import com.sammy.minersdelight.content.block.*;
import com.sammy.minersdelight.content.block.sticky_basket.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.registries.*;
import team.lodestar.lodestone.systems.block.*;
import vectorwing.farmersdelight.common.registry.*;

import java.util.function.*;

public class MDBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MinersDelightMod.MODID);

    public static final Supplier<Block> STICKY_BASKET = BLOCKS.register("sticky_basket",
            () -> new StickyBasketBlock(LodestoneBlockProperties.of().setCutoutRenderType().needsAxe().strength(1.5F).sound(SoundType.BAMBOO_WOOD)));

    public static final Supplier<Block> CAVE_CARROTS = BLOCKS.register("cave_carrots",
            () -> new CaveCarrotBlock(LodestoneBlockProperties.copy(Blocks.CARROTS).setCutoutRenderType()));

    public static final Supplier<Block> CAVE_CARROT_CRATE = BLOCKS.register("cave_carrots",
            () -> new CaveCarrotBlock(LodestoneBlockProperties.copy(ModBlocks.CARROT_CRATE.get()).needsAxe()));

    public static final Supplier<Block> GOSSYPIUM = BLOCKS.register("gossypium",
            () -> new GossypiumFlowerBlock(LodestoneBlockProperties.copy(Blocks.TALL_GRASS).setCutoutRenderType()));
}