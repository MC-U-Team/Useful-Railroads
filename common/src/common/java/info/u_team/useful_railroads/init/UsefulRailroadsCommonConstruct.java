package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;
import info.u_team.useful_railroads.UsefulRailroadsReference;
import info.u_team.useful_railroads.config.CommonConfig;
import net.minecraftforge.fml.ModLoadingContext;

@Construct(modid = UsefulRailroadsReference.MODID)
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
