package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.event.RegisterMenuScreensEvent;
import info.u_team.useful_railroads.screen.TeleportRailScreen;
import info.u_team.useful_railroads.screen.TrackBuilderScreen;
import net.minecraftforge.eventbus.api.IEventBus;

public class UsefulRailroadsScreens {
	
	private static void setup(RegisterMenuScreensEvent event) {
		event.registerScreen(UsefulRailroadsContainerTypes.TELEPORT_RAIL, TeleportRailScreen::new);
		event.registerScreen(UsefulRailroadsContainerTypes.TRACK_BUILDER, TrackBuilderScreen::new);
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(UsefulRailroadsScreens::setup);
	}
	
}
