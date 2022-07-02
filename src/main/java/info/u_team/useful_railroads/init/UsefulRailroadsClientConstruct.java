package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;
import info.u_team.u_team_core.util.registry.BusRegister;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.handler.DrawTrackBuilderSelectionEventHandler;

@Construct(modid = UsefulRailroadsMod.MODID, client = true)
public class UsefulRailroadsClientConstruct implements ModConstruct {
	
	@Override
	public void construct() {
		BusRegister.registerMod(UsefulRailroadsModels::registerMod);
		BusRegister.registerMod(UsefulRailroadsRecipeBookCategories::registerMod);
		BusRegister.registerMod(UsefulRailroadsRenderTypes::registerMod);
		BusRegister.registerMod(UsefulRailroadsScreens::registerMod);
		
		BusRegister.registerForge(DrawTrackBuilderSelectionEventHandler::registerForge);
	}
}
