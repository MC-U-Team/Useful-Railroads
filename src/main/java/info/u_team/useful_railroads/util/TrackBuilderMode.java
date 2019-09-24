package info.u_team.useful_railroads.util;

import java.util.*;
import java.util.stream.Collectors;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.*;

public enum TrackBuilderMode {
	
	MODE_NOAIR("noair", -1, -1),
	MODE_3X3("3x3", 1, 3),
	MODE_5X5("5x5", 2, 5);
	
	private static final Map<String, TrackBuilderMode> NAME_LOOKUP = Arrays.stream(values()).collect(Collectors.toMap(TrackBuilderMode::getName, mode -> mode));
	
	private final String name;
	private final int distanceSide;
	private final int distanceUp;
	
	private TrackBuilderMode(String name, int distanceSide, int distanceUp) {
		this.name = name;
		this.distanceSide = distanceSide;
		this.distanceUp = distanceUp;
	}
	
	public String getName() {
		return name;
	}
	
	public int getDistanceSide() {
		return distanceSide;
	}
	
	public int getDistanceUp() {
		return distanceUp;
	}
	
	@OnlyIn(Dist.CLIENT)
	public String getDisplayString() {
		return I18n.format("container.usefulrailroads.track_builder.mode." + name);
	}
	
	public static TrackBuilderMode byName(String name) {
		return NAME_LOOKUP.getOrDefault(name, MODE_NOAIR);
	}
	
	public static TrackBuilderMode cycle(TrackBuilderMode mode) {
		switch (mode) {
		case MODE_NOAIR:
			return MODE_3X3;
		case MODE_3X3:
			return MODE_5X5;
		case MODE_5X5:
			return MODE_NOAIR;
		}
		return mode;
	}
	
}
