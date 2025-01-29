package info.u_team.useful_railroads;

import info.u_team.u_team_core.util.annotation.AnnotationManager;
import net.neoforged.fml.common.Mod;

@Mod(UsefulRailroadsMod.MODID)
public class UsefulRailroadsMod {
	
	public static final String MODID = UsefulRailroadsReference.MODID;
	
	public UsefulRailroadsMod() {
		AnnotationManager.callAnnotations(MODID);
	}
	
}
