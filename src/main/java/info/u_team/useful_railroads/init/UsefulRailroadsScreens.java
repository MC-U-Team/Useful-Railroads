package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.util.registry.*;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.screen.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class UsefulRailroadsScreens {
	
	@SubscribeEvent
	public static void register(FMLClientSetupEvent event) {
		MainThreadWorker.run(() -> {
			ClientRegistry.registerScreen(UsefulRailroadsContainerTypes.TELEPORT_RAIL, TeleportRailScreen::new);
			ClientRegistry.registerScreen(UsefulRailroadsContainerTypes.TRACK_BUILDER, TrackBuilderScreen::new);
		});
	}
	
}
