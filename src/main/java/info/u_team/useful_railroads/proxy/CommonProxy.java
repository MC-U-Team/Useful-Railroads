package info.u_team.useful_railroads.proxy;

import info.u_team.u_team_core.registry.CommonRegistry;
import info.u_team.useful_railroads.UsefulRailroadsConstants;
import info.u_team.useful_railroads.event.EventHandlerNeighborNotify;
import info.u_team.useful_railroads.handler.UsefulRailroadsGuiHandler;
import info.u_team.useful_railroads.init.*;
import net.minecraftforge.fml.common.event.*;

public class CommonProxy {
	
	public void preinit(FMLPreInitializationEvent event) {
		UsefulRailroadsBlocks.preinit();
	}
	
	public void init(FMLInitializationEvent event) {
		UsefulRailroadsCreativeTabs.init();
		UsefulRailroadsCraftingRecipes.init();
		CommonRegistry.registerGuiHandler(UsefulRailroadsConstants.MODID, new UsefulRailroadsGuiHandler());
		CommonRegistry.registerEventHandler(EventHandlerNeighborNotify.class);
	}
	
	public void postinit(FMLPostInitializationEvent event) {
	}
	
}
