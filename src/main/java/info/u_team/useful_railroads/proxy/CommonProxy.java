package info.u_team.useful_railroads.proxy;

import info.u_team.u_team_core.api.IModProxy;
import info.u_team.useful_railroads.config.CommonExtendedConfig;

public class CommonProxy implements IModProxy {
	
	@Override
	public void construct() {
	}
	
	@Override
	public void setup() {
	}
	
	@Override
	public void complete() {
		CommonExtendedConfig.loadConfig();
	}
	
}
