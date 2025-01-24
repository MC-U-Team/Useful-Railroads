package info.u_team.useful_railroads.config;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.u_team_core.util.ConfigValueHolder;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ForgeCommonConfig {
	
	public static final ForgeConfigSpec CONFIG;
	private static final ForgeCommonConfig INSTANCE;
	
	static {
		final Pair<ForgeCommonConfig, ForgeConfigSpec> pair = new Builder().configure(ForgeCommonConfig::new);
		CONFIG = pair.getRight();
		INSTANCE = pair.getLeft();
	}
	
	public final ConfigValueHolder<Double> highspeedRailMaxSpeed;
	public final ConfigValueHolder<Double> highspeedRailAccelOccupied;
	public final ConfigValueHolder<Double> highspeedRailAccelUnoccupied;
	
	public final ConfigValueHolder<Double> speedClampRailSpeed;
	
	public final ConfigValueHolder<Integer> teleportRailLogDivisionCost;
	public final ConfigValueHolder<Integer> teleportRailDimensionCost;
	
	private ForgeCommonConfig(Builder builder) {
		builder.comment("Rail Settings").push("rail");
		
		// High speed rail config
		builder.comment("High Speed Rail Settings").push("highspeedrail");
		
		final DoubleValue highspeedRailMaxSpeedValue = builder.comment("Maximum Speed for High Speed Rail (default: 5.0 blocks/tick)").defineInRange("highSpeedRailMaxSpeed", 5.0D, 0.0D, 10.0D);
		final DoubleValue highspeedRailAccelOccupiedValue = builder.comment("Acceleration for High Speed Rail if Occupied (default: 4.0 blocks/tick^2)").defineInRange("highSpeedRailAccelOccupied", 4.0D, 0.0D, 10.0D);
		final DoubleValue highspeedRailAccelUnoccupiedValue = builder.comment("Acceleration for High Speed Rail if Unoccupied (default: 2.0 blocks/tick^2)").defineInRange("highSpeedRailAccelUnoccupied", 2.0D, 0.0D, 10.0D);
		
		builder.pop();
		
		// Clamp rail config
		builder.comment("Speed Clamp Rail Settings").push("speedclamprail");
		
		final DoubleValue speedClampRailSpeedValue = builder.comment("Speed for Speed Clamp Rail (default: 0.25 blocks/tick)").defineInRange("speedClampRailSpeed", 0.25D, 0.0D, 10.0D);
		
		builder.pop();
		
		// Teleport rail config
		builder.comment("Teleport Rail Settings").push("teleportrail");
		
		final IntValue teleportRailLogDivisionCostValue = builder.comment("Cost divided by natural log of this value. Lower values increase the cost").defineInRange("teleportRailLogDivisionCost", 5, 2, 100);
		final IntValue teleportRailDimensionCostValue = builder.comment("Extra cost per dimension teleport").defineInRange("teleportRailDimensionCost", 100, 0, 1_000_000);
		
		builder.pop();
		
		builder.pop();
		
		highspeedRailMaxSpeed = new ConfigValueHolder<>(highspeedRailMaxSpeedValue, highspeedRailMaxSpeedValue::set);
		highspeedRailAccelOccupied = new ConfigValueHolder<>(highspeedRailAccelOccupiedValue, highspeedRailAccelOccupiedValue::set);
		highspeedRailAccelUnoccupied = new ConfigValueHolder<>(highspeedRailAccelUnoccupiedValue, highspeedRailAccelUnoccupiedValue::set);
		
		speedClampRailSpeed = new ConfigValueHolder<>(speedClampRailSpeedValue, speedClampRailSpeedValue::set);
		
		teleportRailLogDivisionCost = new ConfigValueHolder<>(teleportRailLogDivisionCostValue, teleportRailLogDivisionCostValue::set);
		teleportRailDimensionCost = new ConfigValueHolder<>(teleportRailDimensionCostValue, teleportRailDimensionCostValue::set);
	}
	
	public static class Impl extends CommonConfig {
		
		@Override
		public ConfigValueHolder<Double> highspeedRailMaxSpeed() {
			return INSTANCE.highspeedRailMaxSpeed;
		}
		
		@Override
		public ConfigValueHolder<Double> highspeedRailAccelOccupied() {
			return INSTANCE.highspeedRailAccelOccupied;
		}
		
		@Override
		public ConfigValueHolder<Double> highspeedRailAccelUnoccupied() {
			return INSTANCE.highspeedRailAccelUnoccupied;
		}
		
		@Override
		public ConfigValueHolder<Double> speedClampRailSpeed() {
			return INSTANCE.speedClampRailSpeed;
		}
		
		@Override
		public ConfigValueHolder<Integer> teleportRailLogDivisionCost() {
			return INSTANCE.teleportRailLogDivisionCost;
		}
		
		@Override
		public ConfigValueHolder<Integer> teleportRailDimensionCost() {
			return INSTANCE.teleportRailDimensionCost;
		}
		
	}
	
}
