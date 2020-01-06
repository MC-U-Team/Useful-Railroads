package info.u_team.useful_railroads.init;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.BUFFER_STOP;

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
		final RenderType cutout = RenderType.func_228643_e_();
		
		RenderTypeLookup.setRenderLayer(BUFFER_STOP, cutout);
	}
	
}
