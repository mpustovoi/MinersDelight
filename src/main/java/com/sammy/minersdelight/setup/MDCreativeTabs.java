package com.sammy.minersdelight.setup;

import com.sammy.minersdelight.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.*;

public class MDCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MinersDelightMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CONTENT = CREATIVE_MODE_TABS.register("minersdelight",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + MinersDelightMod.MODID))
                    .withTabsBefore(ResourceLocation.parse("farmersdelight:farmersdelight"))
                    .icon(() -> MDItems.COPPER_POT.get().getDefaultInstance()).build()
    );
}
