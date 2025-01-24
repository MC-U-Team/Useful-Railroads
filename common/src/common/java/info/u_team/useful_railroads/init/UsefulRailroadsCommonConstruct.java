package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.config.CommonConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

@Construct(modid = UsefulRailroadsMod.MODID)
public class UsefulRailroadsCommonConstruct implements ModConstruct {
	
	@Override
	public void construct() {
		ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.CONFIG);
		
		UsefulRailroadsBlocks.register();
		UsefulRailroadsCreativeTabs.register();
		UsefulRailroadsDataComponentTypes.register();
		UsefulRailroadsItems.register();
		UsefulRailroadsMenuTypes.register();
		UsefulRailroadsRecipeSerializers.register();
		UsefulRailroadsRecipeTypes.register();
		UsefulRailroadsBlockEntityTypes.register();
	}
}
