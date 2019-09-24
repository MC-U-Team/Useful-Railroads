package info.u_team.useful_railroads.init;

import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.gui.*;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class UsefulRailroadsGuis {
	
	@SubscribeEvent
	public static void register(FMLClientSetupEvent event) {
		ScreenManager.registerFactory(UsefulRailroadsContainerTypes.TELEPORT_RAIL, TeleportRailScreen::new);
		ScreenManager.registerFactory(UsefulRailroadsContainerTypes.TRACK_BUILDER, TrackBuilderScreen::new);
	}
	
}
