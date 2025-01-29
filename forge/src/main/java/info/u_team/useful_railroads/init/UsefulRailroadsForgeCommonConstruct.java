package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;
import info.u_team.useful_railroads.UsefulRailroadsReference;
import info.u_team.useful_railroads.config.ForgeCommonConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

@Construct(modid = UsefulRailroadsReference.MODID)
public class UsefulRailroadsForgeCommonConstruct implements ModConstruct {
	
	@SuppressWarnings("removal") // TODO remove code later
	@Override
	public void construct() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ForgeCommonConfig.CONFIG);
	}
}
