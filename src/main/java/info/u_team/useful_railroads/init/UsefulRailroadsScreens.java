package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.registry.client.MenuScreenRegister;
import info.u_team.useful_railroads.screen.TeleportRailScreen;
import info.u_team.useful_railroads.screen.TrackBuilderScreen;
import net.minecraft.Util;

public class UsefulRailroadsScreens {
	
	private static final MenuScreenRegister MENU_SCREENS = Util.make(MenuScreenRegister.create(), menuScreens -> {
		menuScreens.register(UsefulRailroadsMenuTypes.TELEPORT_RAIL, TeleportRailScreen::new);
		menuScreens.register(UsefulRailroadsMenuTypes.TRACK_BUILDER, TrackBuilderScreen::new);
	});
	
	static void register() {
		MENU_SCREENS.register();
	}
	
}
