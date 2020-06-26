package info.u_team.useful_railroads;

import info.u_team.u_team_core.util.verify.JarSignVerifier;
import info.u_team.useful_railroads.config.UsefulRailroadsConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(UsefulRailroadsMod.MODID)
public class UsefulRailroadsMod {
	
	public static final String MODID = "usefulrailroads";
	
	public UsefulRailroadsMod() {
		JarSignVerifier.checkSigned(MODID);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UsefulRailroadsConfig.COMMON_CONFIG);
		register();
	}
	
	private void register() {
	}
	
}
