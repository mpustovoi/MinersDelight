package com.sammy.minersdelight.setup;

import com.sammy.minersdelight.MinersDelightMod;
import com.sammy.minersdelight.content.block.copper_pot.CopperPotMenu;
import com.sammy.minersdelight.content.block.copper_pot.CopperPotScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MDMenuTypes {


    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, MinersDelightMod.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<CopperPotMenu>> COPPER_POT = MENU_TYPES
            .register("copper_pot", () -> IMenuTypeExtension.create(CopperPotMenu::new));

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = MinersDelightMod.MODID)
    public static class ClientOnly {

        @SubscribeEvent
        public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
            event.register(COPPER_POT.get(), CopperPotScreen::new);
        }
    }
}