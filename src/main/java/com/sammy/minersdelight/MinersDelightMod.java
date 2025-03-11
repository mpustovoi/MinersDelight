package com.sammy.minersdelight;

import com.sammy.minersdelight.datagen.*;
import com.sammy.minersdelight.setup.*;
import net.minecraft.data.*;
import net.minecraft.resources.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.*;
import net.neoforged.fml.common.*;
import org.apache.logging.log4j.*;

import java.util.*;

@Mod(MinersDelightMod.MODID)
public class MinersDelightMod {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "minersdelight";
	public static final Random RANDOM = new Random();

	public MinersDelightMod(IEventBus modEventBus, ModContainer modContainer) {
		MDLootModifiers.LOOT_MODIFIERS.register(modEventBus);

		MDLootConditions.LOOT_CONDITIONS.register(modBus);
		MDMenuTypes.MENU_TYPES.register(modBus);
		MDBlocks.register();
		MDItems.register();
		MDBlockEntities.register();
		modEventBus.addListener(MDCauldronInteractions::addCauldronInteractions);
		modBus.addListener(MDComposting::addCompostValues);
		modBus.addListener(DataOnly::gatherData);
	}

	public static ResourceLocation path(String path) {
		return new ResourceLocation(MODID, path);
	}

	public static class DataOnly {
		public static void gatherData(GatherDataEvent event) {
			DataGenerator generator = event.getGenerator();
			generator.addProvider(new MDLangMerger(generator));
			generator.addProvider(new MDRecipeProvider(generator));
		}
	}
}