package com.sammy.minersdelight.setup;

import com.sammy.minersdelight.MinersDelightMod;
import com.sammy.minersdelight.content.block.copper_pot.CopperPotMenu;
import com.sammy.minersdelight.content.block.copper_pot.CopperPotScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.common.extensions.IForgeMenuType;
import net.neoforged.eventbus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.registries.DeferredRegister;
import net.neoforged.registries.ForgeRegistries;
import net.neoforged.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.client.recipebook.RecipeCategories;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.registry.ModMenuTypes;

public class MDMenuTypes {


    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, MinersDelightMod.MODID);

    public static final RegistryObject<MenuType<CopperPotMenu>> COPPER_POT = MENU_TYPES
            .register("copper_pot", () -> IForgeMenuType.create(CopperPotMenu::new));

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MinersDelightMod.MODID)
    public static class ClientOnly {

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> MenuScreens.register(COPPER_POT.get(), CopperPotScreen::new));
        }
    }
}