package info.u_team.useful_railroads.init;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.BUFFER_STOP;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.DIRECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.HIGHSPEED_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.INTERSECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.SPEED_CLAMP_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.TELEPORT_RAIL;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class UsefulRailroadsRenderTypes {
	
	private static void setup(FMLClientSetupEvent event) {
		// Cutout
		final RenderType cutout = RenderType.getCutout();
		
		RenderTypeLookup.setRenderLayer(HIGHSPEED_RAIL.get(), cutout);
		RenderTypeLookup.setRenderLayer(SPEED_CLAMP_RAIL.get(), cutout);
		RenderTypeLookup.setRenderLayer(DIRECTION_RAIL.get(), cutout);
		RenderTypeLookup.setRenderLayer(INTERSECTION_RAIL.get(), cutout);
		RenderTypeLookup.setRenderLayer(TELEPORT_RAIL.get(), cutout);
		RenderTypeLookup.setRenderLayer(BUFFER_STOP.get(), cutout);
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(UsefulRailroadsRenderTypes::setup);
	}
	
}
