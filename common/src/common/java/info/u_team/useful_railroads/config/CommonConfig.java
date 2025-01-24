package info.u_team.useful_railroads.config;

import info.u_team.u_team_core.util.ConfigValueHolder;
import info.u_team.u_team_core.util.ServiceUtil;

public abstract class CommonConfig {
	
	private static final CommonConfig INSTANCE = ServiceUtil.loadOne(CommonConfig.class);
	
	public static CommonConfig getInstance() {
		return INSTANCE;
	}
	
	public abstract ConfigValueHolder<Double> highspeedRailMaxSpeed();
	
	public abstract ConfigValueHolder<Double> highspeedRailAccelOccupied();
	
	public abstract ConfigValueHolder<Double> highspeedRailAccelUnoccupied();
	
	public abstract ConfigValueHolder<Double> speedClampRailSpeed();
	
	public abstract ConfigValueHolder<Integer> teleportRailLogDivisionCost();
	
	public abstract ConfigValueHolder<Integer> teleportRailDimensionCost();
	
}
