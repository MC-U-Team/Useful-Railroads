package info.u_team.useful_railroads.proxy;

import info.u_team.u_team_core.api.IModProxy;
import info.u_team.useful_railroads.config.CommonConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class CommonProxy implements IModProxy {
	
	@Override
	public void construct() {
		ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.CONFIG);
	}
	
	@Override
	public void setup() {
	}
	
	@Override
	public void complete() {
	}
	
}
