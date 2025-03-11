package com.sammy.minersdelight.content.item;

import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

import static net.minecraft.world.level.block.InfestedBlock.*;

public class SilverfishEggsItem extends Item {
    public SilverfishEggsItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        var level = pContext.getLevel();
        var player = pContext.getPlayer();
        var hand = pContext.getHand();
        var pos = pContext.getClickedPos();
        var state = level.getBlockState(pos);
        if (isCompatibleHostBlock(state)) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            }
            BlockState infestedState = infestedStateByHost(state);
            level.setBlock(pos, infestedState, 3);
            level.levelEvent(2001, pos, Block.getId(infestedState));
            if (player != null) {
                player.swing(hand, true);
                if (!player.isCreative()) {
                    player.getItemInHand(hand).shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(pContext);
    }
}