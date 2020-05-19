package info.u_team.useful_railroads;

import info.u_team.u_team_core.util.verify.JarSignVerifier;
import info.u_team.useful_railroads.config.Config;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(UsefulRailroadsMod.MODID)
public class UsefulRailroadsMod {
	
	public static final String MODID = "usefulrailroads";
	
	public UsefulRailroadsMod() {
		JarSignVerifier.checkSigned(MODID);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

		Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("usefulrailroads-common.toml"));

	}
	
}
