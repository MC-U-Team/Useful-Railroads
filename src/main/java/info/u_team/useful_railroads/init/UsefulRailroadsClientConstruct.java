package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.construct.*;
import info.u_team.u_team_core.util.registry.BusRegister;
import info.u_team.useful_railroads.UsefulRailroadsMod;

@Construct(modid = UsefulRailroadsMod.MODID, client = true)
public class UsefulRailroadsClientConstruct implements IModConstruct {
	
	@Override
	public void construct() {
		BusRegister.registerMod(UsefulRailroadsModels::registerMod);
		BusRegister.registerMod(UsefulRailroadsRenderTypes::registerMod);
	}
}
