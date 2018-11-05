package info.u_team.useful_railroads.proxy;

import info.u_team.useful_railroads.init.*;
import net.minecraftforge.fml.common.event.*;

public class CommonProxy {
	
	public void preinit(FMLPreInitializationEvent event) {
		UsefulRailroadsBlocks.preinit();
	}
	
	public void init(FMLInitializationEvent event) {
		UsefulRailroadsCreativeTabs.init();
	}
	
	public void postinit(FMLPostInitializationEvent event) {
	}
	
}
