package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;
import info.u_team.useful_railroads.UsefulRailroadsReference;
import info.u_team.useful_railroads.config.NeoForgeCommonConfig;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;

@Construct(modid = UsefulRailroadsReference.MODID)
public class UsefulRailroadsNeoForgeCommonConstruct implements ModConstruct {
	
	@Override
	public void construct() {
		ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, NeoForgeCommonConfig.CONFIG);
		
		UsefulRailroadsNeoForgeCapabilities.register();
	}
}
