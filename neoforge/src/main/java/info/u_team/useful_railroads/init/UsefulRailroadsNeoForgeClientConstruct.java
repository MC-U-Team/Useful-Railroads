package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;
import info.u_team.useful_railroads.UsefulRailroadsReference;

@Construct(modid = UsefulRailroadsReference.MODID, client = true)
public class UsefulRailroadsNeoForgeClientConstruct implements ModConstruct {
	
	@Override
	public void construct() {
		UsefulRailroadsNeoForgeRecipeBookCategories.register();
	}
}
