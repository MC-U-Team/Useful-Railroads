package info.u_team.useful_railroads.data;

import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.data.provider.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.MOD)
public class UsefulRailroadsDataGenerator {
	
	@SubscribeEvent
	public static void data(GatherDataEvent event) {
		final DataGenerator generator = event.getGenerator();
		if (event.includeServer()) {
			generator.addProvider(new UsefulRailroadsBlockTagsProvider(generator)); // Generate block tags
			generator.addProvider(new UsefulRailroadsItemTagsProvider(generator)); // Generate item tags
			
			generator.addProvider(new UsefulRailroadsLootTableProvider(generator)); // Generate loot tables
			generator.addProvider(new UsefulRailroadsRecipesProvider(generator)); // Generate recipes
		}
	}
	
}
