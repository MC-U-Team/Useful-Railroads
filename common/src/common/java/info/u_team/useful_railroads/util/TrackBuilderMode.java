package info.u_team.useful_railroads.util;

import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

public enum TrackBuilderMode implements StringRepresentable {
	
	MODE_NOAIR(0, "noair", -1, -1),
	MODE_3X3(1, "3x3", 1, 3),
	MODE_5X5(2, "5x5", 2, 5),
	MODE_TUNNEL(3, "tunnel", -1, -1);
	
	public static final Codec<TrackBuilderMode> CODEC = StringRepresentable.fromEnum(TrackBuilderMode::values);
	
	public static final StreamCodec<ByteBuf, TrackBuilderMode> STREAM_CODEC = ByteBufCodecs.idMapper(ByIdMap.continuous(TrackBuilderMode::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO), TrackBuilderMode::getId);
	
	private final int id;
	private final String name;
	private final int distanceSide;
	private final int distanceUp;
	private final Component displayComponent;
	
	private TrackBuilderMode(int id, String name, int distanceSide, int distanceUp) {
		this.id = id;
		this.name = name;
		this.distanceSide = distanceSide;
		this.distanceUp = distanceUp;
		displayComponent = Component.translatable("container.usefulrailroads.track_builder.mode." + name);
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String getSerializedName() {
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
