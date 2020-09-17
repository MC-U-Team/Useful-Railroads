package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.construct.*;
import info.u_team.u_team_core.util.registry.BusRegister;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.config.*;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

@Construct(modid = UsefulRailroadsMod.MODID)
public class UsefulRailroadsCommonConstruct implements IModConstruct {
	
	@Override
	public void construct() {
		ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.CONFIG);
		ModLoadingContext.get().registerConfig(Type.SERVER, ServerConfig.CONFIG);
		
		BusRegister.registerMod(UsefulRailroadsBlocks::registerMod);
		BusRegister.registerMod(UsefulRailroadsContainerTypes::registerMod);
		BusRegister.registerMod(UsefulRailroadsItems::registerMod);
		BusRegister.registerMod(UsefulRailroadsRecipeSerializers::registerMod);
		BusRegister.registerMod(UsefulRailroadsTileEntityTypes::registerMod);
	}
}
