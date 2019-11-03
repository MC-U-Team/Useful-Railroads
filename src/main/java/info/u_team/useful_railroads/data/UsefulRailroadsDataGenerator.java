package info.u_team.useful_railroads.data;

import info.u_team.u_team_core.data.GenerationData;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.data.provider.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.MOD)
public class UsefulRailroadsDataGenerator {
	
	@SubscribeEvent
	public static void data(GatherDataEvent event) {
		final GenerationData data = new GenerationData(UsefulRailroadsMod.MODID, event);
		if (event.includeServer()) {
			data.addProvider(UsefulRailroadsBlockTagsProvider::new);
			data.addProvider(UsefulRailroadsItemTagsProvider::new);
			data.addProvider(UsefulRailroadsLootTableProvider::new);
			data.addProvider(UsefulRailroadsRecipesProvider::new);
		}
	}
	
}
