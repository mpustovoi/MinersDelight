package com.sammy.minersdelight.setup;

import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.neoforged.fml.event.lifecycle.*;

import static net.minecraft.core.cauldron.CauldronInteraction.*;

public class MDCauldronInteractions {

    public static void addCauldronInteractions(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

            WATER.map().put(MDItems.COPPER_CUP.get(), (state, level, pos, player, hand, stack) ->
                    fillBucket(state, level, pos, player, hand, stack, new ItemStack(MDItems.WATER_CUP.get()),
                            s -> s.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL));
            POWDER_SNOW.map().put(MDItems.COPPER_CUP.get(), (state, level, pos, player, hand, stack) ->
                    fillBucket(state, level, pos, player, hand, stack, new ItemStack(MDItems.POWDERED_SNOW_CUP.get()),
                            s -> s.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL_POWDER_SNOW));

            EMPTY.map().put(MDItems.WATER_CUP.get(), (state, level, pos, player, hand, stack) -> emptyCup(
                    level, pos, player, hand, stack,
                    Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3),
                    SoundEvents.BUCKET_EMPTY
            ));
            EMPTY.map().put(MDItems.POWDERED_SNOW_CUP.get(), (state, level, pos, player, hand, stack) -> emptyCup(
                    level, pos, player, hand, stack,
                    Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3),
                    SoundEvents.BUCKET_EMPTY_POWDER_SNOW
            ));
        });
    }

    static ItemInteractionResult emptyCup(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack filledStack, BlockState state, SoundEvent emptySound) {
        if (!level.isClientSide) {
            Item item = filledStack.getItem();
            player.setItemInHand(hand, ItemUtils.createFilledResult(filledStack, player, MDItems.COPPER_CUP.get().getDefaultInstance()));
            player.awardStat(Stats.FILL_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(item));
            level.setBlockAndUpdate(pos, state);
            level.playSound(null, pos, emptySound, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }
}