package info.u_team.useful_railroads.init;

import info.u_team.useful_railroads.gui.TeleportRailScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class UsefulRailroadsGuis {
	
	public static void construct() {
		ScreenManager.registerFactory(UsefulRailroadsContainers.TELEPORT_RAIL, TeleportRailScreen::new);
	}
	
}
