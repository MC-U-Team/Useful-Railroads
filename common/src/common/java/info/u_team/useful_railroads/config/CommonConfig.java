package info.u_team.useful_railroads.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class CommonConfig {
	
	public static final ForgeConfigSpec CONFIG;
	private static final CommonConfig INSTANCE;
	
	static {
		final Pair<CommonConfig, ForgeConfigSpec> pair = new Builder().configure(CommonConfig::new);
		CONFIG = pair.getRight();
		INSTANCE = pair.getLeft();
	}
	
	public static CommonConfig getInstance() {
		return INSTANCE;
	}
	
	public final DoubleValue highspeedRailMaxSpeed;
	public final DoubleValue highspeedRailAccelOccupied;
	public final DoubleValue highspeedRailAccelUnoccupied;
	
	public final DoubleValue speedClampRailSpeed;
	
	public final IntValue teleportRailLogDivisionCost;
	public final IntValue teleportRailDimensionCost;
	
	private CommonConfig(Builder builder) {
		builder.comment("Rail Settings").push("rail");
		
		// High speed rail config
		builder.comment("High Speed Rail Settings").push("highspeedrail");
		
		highspeedRailMaxSpeed = builder.comment("Maximum Speed for High Speed Rail (default: 5.0 blocks/tick)").defineInRange("highSpeedRailMaxSpeed", 5.0D, 0.0D, 10.0D);
		highspeedRailAccelOccupied = builder.comment("Acceleration for High Speed Rail if Occupied (default: 4.0 blocks/tick^2)").defineInRange("highSpeedRailAccelOccupied", 4.0D, 0.0D, 10.0D);
		highspeedRailAccelUnoccupied = builder.comment("Acceleration for High Speed Rail if Unoccupied (default: 2.0 blocks/tick^2)").defineInRange("highSpeedRailAccelUnoccupied", 2.0D, 0.0D, 10.0D);
		
		builder.pop();
		
		// Clamp rail config
		builder.comment("Speed Clamp Rail Settings").push("speedclamprail");
		
		speedClampRailSpeed = builder.comment("Speed for Speed Clamp Rail (default: 0.25 blocks/tick)").defineInRange("speedClampRailSpeed", 0.25D, 0.0D, 10.0D);
		
		builder.pop();
		
		// Teleport rail config
		builder.comment("Teleport Rail Settings").push("teleportrail");
		
		teleportRailLogDivisionCost = builder.comment("Cost divided by natural log of this value. Lower values increase the cost").defineInRange("teleportRailLogDivisionCost", 5, 2, 100);
		teleportRailDimensionCost = builder.comment("Extra cost per dimension teleport").defineInRange("teleportRailDimensionCost", 100, 0, 1_000_000);
		
		builder.pop();
		
		builder.pop();
	}
	
}
