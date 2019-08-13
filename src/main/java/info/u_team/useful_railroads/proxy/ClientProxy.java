package info.u_team.useful_railroads.proxy;

import info.u_team.useful_railroads.init.UsefulRailroadsGuis;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void construct() {
		super.construct();
		UsefulRailroadsGuis.construct();
	}
	
	@Override
	public void setup() {
		super.setup();
	}
	
	@Override
	public void complete() {
		super.complete();
	}
	
}
