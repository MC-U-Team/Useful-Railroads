package info.u_team.useful_railroads.util;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.network.chat.Component;

public enum TrackBuilderMode {
	
	MODE_NOAIR("noair", -1, -1),
	MODE_3X3("3x3", 1, 3),
	MODE_5X5("5x5", 2, 5),
	MODE_TUNNEL("tunnel", -1, -1);
	
	private static final Map<String, TrackBuilderMode> NAME_LOOKUP = Arrays.stream(values()).collect(Collectors.toMap(TrackBuilderMode::getName, mode -> mode));
	
	private final String name;
	private final int distanceSide;
	private final int distanceUp;
	private final Component displayComponent;
	
	private TrackBuilderMode(String name, int distanceSide, int distanceUp) {
		this.name = name;
		this.distanceSide = distanceSide;
		this.distanceUp = distanceUp;
		displayComponent = Component.translatable("container.usefulrailroads.track_builder.mode." + name);
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
	
	public boolean isFullTunnel() {
		return this == MODE_TUNNEL;
	}
	
	public boolean isNoTunnel() {
		return this == MODE_NOAIR;
	}
	
	public Component getDisplayComponent() {
		return displayComponent;
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
			return MODE_TUNNEL;
		case MODE_TUNNEL:
			return MODE_NOAIR;
		}
		return mode;
	}
	
}
