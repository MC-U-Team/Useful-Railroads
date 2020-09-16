package info.u_team.useful_railroads;

import info.u_team.u_team_core.util.annotation.AnnotationManager;
import info.u_team.u_team_core.util.verify.JarSignVerifier;
import net.minecraftforge.fml.common.Mod;

@Mod(UsefulRailroadsMod.MODID)
public class UsefulRailroadsMod {
	
	public static final String MODID = "usefulrailroads";
	
	public UsefulRailroadsMod() {
		JarSignVerifier.checkSigned(MODID);
		
		AnnotationManager.callAnnotations(MODID);
	}
	
//	public UsefulRailroadsMod() {
//		JarSignVerifier.checkSigned(MODID);
//		ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.CONFIG);
//		ModLoadingContext.get().registerConfig(Type.SERVER, ServerConfig.CONFIG);
//		register();
//	}
	
//	private void register() {
//		BusRegister.registerMod(UsefulRailroadsBlocks::register);
//		BusRegister.registerMod(UsefulRailroadsContainerTypes::register);
//		BusRegister.registerMod(UsefulRailroadsItems::register);
//		BusRegister.registerMod(UsefulRailroadsRecipeSerializers::register);
//		BusRegister.registerMod(UsefulRailroadsTileEntityTypes::register);
//	}
	
}
