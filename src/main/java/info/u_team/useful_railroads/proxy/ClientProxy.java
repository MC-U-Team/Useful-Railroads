package info.u_team.useful_railroads.proxy;

import info.u_team.u_team_core.registry.CommonRegistry;
import info.u_team.useful_railroads.event.EventHandlerConfigChange;
import net.minecraftforge.fml.common.event.*;

public class ClientProxy extends CommonProxy {
	
	public void preinit(FMLPreInitializationEvent event) {
		super.preinit(event);
	}
	
	public void init(FMLInitializationEvent event) {
		super.init(event);
		CommonRegistry.registerEventHandler(EventHandlerConfigChange.class);
	}
	
	public void postinit(FMLPostInitializationEvent event) {
		super.postinit(event);
	}
	
}
