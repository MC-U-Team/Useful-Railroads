package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.util.registry.ClientRegistry;
import info.u_team.useful_railroads.screen.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class UsefulRailroadsScreens {
	
	private static void setup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ClientRegistry.registerScreen(UsefulRailroadsContainerTypes.TELEPORT_RAIL, TeleportRailScreen::new);
			ClientRegistry.registerScreen(UsefulRailroadsContainerTypes.TRACK_BUILDER, TrackBuilderScreen::new);
		});
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(UsefulRailroadsScreens::setup);
	}
	
}
