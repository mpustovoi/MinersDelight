package com.sammy.minersdelight.datagen;

import com.sammy.minersdelight.*;
import net.minecraft.data.*;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.data.*;
import team.lodestar.lodestone.systems.datagen.providers.*;
import team.lodestar.lodestone.systems.datagen.statesmith.*;

import javax.annotation.*;
import java.util.*;
import java.util.function.*;

import static com.sammy.minersdelight.setup.MDBlocks.*;

public class MDBlockStates extends LodestoneBlockStateProvider {

    public MDBlockStates(PackOutput output, ExistingFileHelper exFileHelper, LodestoneItemModelProvider itemModelProvider) {
        super(output, MinersDelightMod.MODID, exFileHelper, itemModelProvider);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Malum BlockStates";
    }

    @Override
    protected void registerStatesAndModels() {
        Set<Supplier<? extends Block>> blocks = new HashSet<>(BLOCKS.getEntries());

        AbstractBlockStateSmith.StateSmithData data = new AbstractBlockStateSmith.StateSmithData(this, blocks::remove);

    }
}