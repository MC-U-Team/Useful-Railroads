package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;
import info.u_team.useful_railroads.UsefulRailroadsReference;
import info.u_team.useful_railroads.handler.DrawTrackBuilderSelectionEventHandler;

@Construct(modid = UsefulRailroadsReference.MODID, client = true)
public class UsefulRailroadsClientConstruct implements ModConstruct {
	
	@Override
	public void construct() {
		UsefulRailroadsModels.register();
		UsefulRailroadsScreens.register();
		
		DrawTrackBuilderSelectionEventHandler.register();
	}
}
