package info.u_team.useful_railroads;

import info.u_team.u_team_core.util.registry.BusRegister;
import info.u_team.u_team_core.util.verify.JarSignVerifier;
import info.u_team.useful_railroads.config.UsefulRailroadsConfig;
import info.u_team.useful_railroads.init.*;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

@Mod(UsefulRailroadsMod.MODID)
public class UsefulRailroadsMod {
	
	public static final String MODID = "usefulrailroads";
	
	public UsefulRailroadsMod() {
		JarSignVerifier.checkSigned(MODID);
		ModLoadingContext.get().registerConfig(Type.COMMON, UsefulRailroadsConfig.COMMON_CONFIG);
		register();
	}
	
	private void register() {
		BusRegister.registerMod(UsefulRailroadsBlocks::register);
		BusRegister.registerMod(UsefulRailroadsContainerTypes::register);
		BusRegister.registerMod(UsefulRailroadsItems::register);
		BusRegister.registerMod(UsefulRailroadsRecipeSerializers::register);
		BusRegister.registerMod(UsefulRailroadsTileEntityTypes::register);
	}
	
}
