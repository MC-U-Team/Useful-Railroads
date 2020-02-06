package info.u_team.useful_railroads;

import org.apache.logging.log4j.*;

public class UsefulRailroadsConstants {
	
	public static final String MODID = "usefulrailroads";
	public static final String NAME = "Useful Railroads";
	public static final String VERSION = "${version}";
	public static final String MCVERSION = "${mcversion}";
	public static final String DEPENDENCIES = "required:forge@[14.23.5.2768,);required-after:uteamcore@[2.2.5.147,)";
	public static final String UPDATEURL = "https://api.u-team.info/update/usefulrailroads.json";
	
	public static final String COMMONPROXY = "info.u_team.useful_railroads.proxy.CommonProxy";
	public static final String CLIENTPROXY = "info.u_team.useful_railroads.proxy.ClientProxy";
	
	public static final Logger LOGGER = LogManager.getLogger(NAME);
	
	private UsefulRailroadsConstants() {
	}
}
