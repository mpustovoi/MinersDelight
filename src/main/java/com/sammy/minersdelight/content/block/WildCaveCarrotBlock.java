package com.sammy.minersdelight.content.block;

import net.minecraft.core.*;
import net.minecraft.tags.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import vectorwing.farmersdelight.common.block.*;

public class WildCaveCarrotBlock extends WildCropBlock {
    public WildCaveCarrotBlock(Properties properties) {
        super(MobEffects.DIG_SPEED, 10, properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.BASE_STONE_NETHER) || state.is(BlockTags.DIRT) || state.is(BlockTags.SAND);
    }
}
