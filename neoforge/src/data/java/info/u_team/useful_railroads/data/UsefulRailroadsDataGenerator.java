package info.u_team.useful_railroads.data;

import info.u_team.u_team_core.data.GenerationData;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.data.provider.UsefulRailroadsBlockStateProvider;
import info.u_team.useful_railroads.data.provider.UsefulRailroadsBlockTagsProvider;
import info.u_team.useful_railroads.data.provider.UsefulRailroadsItemModelProvider;
import info.u_team.useful_railroads.data.provider.UsefulRailroadsItemTagsProvider;
import info.u_team.useful_railroads.data.provider.UsefulRailroadsLanguagesProvider;
import info.u_team.useful_railroads.data.provider.UsefulRailroadsLootTableProvider;
import info.u_team.useful_railroads.data.provider.UsefulRailroadsRecipeProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.MOD)
public class UsefulRailroadsDataGenerator {
	
	@SubscribeEvent
	public static void data(GatherDataEvent event) {
		final GenerationData data = new GenerationData(UsefulRailroadsMod.MODID, event);
		data.addProvider(event.includeServer(), UsefulRailroadsBlockTagsProvider::new, UsefulRailroadsItemTagsProvider::new);
		data.addProvider(event.includeServer(), UsefulRailroadsLootTableProvider::new);
		data.addProvider(event.includeServer(), UsefulRailroadsRecipeProvider::new);
		
		data.addProvider(event.includeClient(), UsefulRailroadsItemModelProvider::new);
		data.addProvider(event.includeClient(), UsefulRailroadsBlockStateProvider::new);
		data.addProvider(event.includeClient(), UsefulRailroadsLanguagesProvider::new);
	}
}
