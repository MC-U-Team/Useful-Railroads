package info.u_team.useful_railroads.init;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.*;

import info.u_team.useful_railroads.UsefulRailroadsMod;
import net.minecraft.client.renderer.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class UsefulRailroadsRenderTypes {
	
	@SubscribeEvent
	public static void register(FMLClientSetupEvent event) {
		// Cutout
		final RenderType cutout = RenderType.getCutout();
		
		RenderTypeLookup.setRenderLayer(HIGHSPEED_RAIL.get(), cutout);
		RenderTypeLookup.setRenderLayer(SPEED_CLAMP_RAIL.get(), cutout);
		RenderTypeLookup.setRenderLayer(DIRECTION_RAIL.get(), cutout);
		RenderTypeLookup.setRenderLayer(INTERSECTION_RAIL.get(), cutout);
		RenderTypeLookup.setRenderLayer(TELEPORT_RAIL.get(), cutout);
		
		RenderTypeLookup.setRenderLayer(BUFFER_STOP.get(), cutout);
	}
	
}
