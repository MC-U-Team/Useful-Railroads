package info.u_team.useful_railroads.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

public class UsefulRailroadsConfig {
	public static final String CATEGORY_RAIL = "rail";
	public static final String SUBCATEGORY_RAIL_HIGHSPEED = "highspeedrail";
	public static final String SUBCATEGORY_RAIL_SPEEDCLAMP = "speedclamprail";

	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

	public static ForgeConfigSpec COMMON_CONFIG;

	public static ForgeConfigSpec.DoubleValue HIGH_SPEED_RAIL_MAX_SPEED;
	public static ForgeConfigSpec.DoubleValue HIGH_SPEED_RAIL_ACCEL_OCCUPIED;
	public static ForgeConfigSpec.DoubleValue HIGH_SPEED_RAIL_ACCEL_UNOCCUPIED;
	public static ForgeConfigSpec.DoubleValue SPEED_CLAMP_RAIL_SPEED;

	static {
		// build config file structure
		COMMON_BUILDER.comment("Rail Settings").push(CATEGORY_RAIL);

		setupHighSpeedConfig();
		setupSpeedClampConfig();

		COMMON_BUILDER.pop();

		COMMON_CONFIG = COMMON_BUILDER.build();
	}

	private static void setupHighSpeedConfig() {
		COMMON_BUILDER.comment("High Speed Rail Settings").push(SUBCATEGORY_RAIL_HIGHSPEED);

		HIGH_SPEED_RAIL_MAX_SPEED = COMMON_BUILDER
				.comment("Maximum Speed for High Speed Rail (default: 5.0)")
				.defineInRange("highSpeedRailMaxSpeed", 5.0D, 0.0D, 10.0D);
		HIGH_SPEED_RAIL_ACCEL_OCCUPIED = COMMON_BUILDER
				.comment("Acceleration for High Speed Rail if Occupied (default: 4.0)")
				.defineInRange("highSpeedRailAccelOccupied", 4.0D, 0.0D, 10.0D);
		HIGH_SPEED_RAIL_ACCEL_UNOCCUPIED = COMMON_BUILDER
				.comment("Acceleration for High Speed Rail if Unoccupied (default: 2.0)")
				.defineInRange("highSpeedRailAccelUnoccupied", 2.0D, 0.0D, 10.0D);

		COMMON_BUILDER.pop();
	}

	private static void setupSpeedClampConfig() {
		COMMON_BUILDER.comment("Speed Clamp Rail Settings").push(SUBCATEGORY_RAIL_SPEEDCLAMP);

		SPEED_CLAMP_RAIL_SPEED = COMMON_BUILDER.comment("Speed for Speed Clamp Rail (default: 0.25)")
				.defineInRange("speedClampRailSpeed", 0.25D, 0.0D, 10.0D);

		COMMON_BUILDER.pop();
	}

	public static void loadConfig(ForgeConfigSpec spec, Path path) {

		final CommentedFileConfig configData = CommentedFileConfig.builder(path)
				.sync()
				.autosave()
				.writingMode(WritingMode.REPLACE)
				.build();

		configData.load();
		spec.setConfig(configData);
	}
}
