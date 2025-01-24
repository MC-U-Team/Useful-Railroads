package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.registry.DataComponentTypeRegister;
import info.u_team.u_team_core.api.registry.RegistryEntry;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.component.TrackBuilderComponent;
import net.minecraft.core.component.DataComponentType;

public class UsefulRailroadsDataComponentTypes {
	
	public static final DataComponentTypeRegister DATA_COMPONENT_TYPES = DataComponentTypeRegister.create(UsefulRailroadsMod.MODID);
	
	public static final RegistryEntry<DataComponentType<TrackBuilderComponent>> TRACK_BUILDER = DATA_COMPONENT_TYPES.register("track_builder", () -> {
		return DataComponentType.<TrackBuilderComponent> builder().persistent(TrackBuilderComponent.CODEC).networkSynchronized(TrackBuilderComponent.STREAM_CODEC).cacheEncoding();
	});
	
	static void register() {
		DATA_COMPONENT_TYPES.register();
	}
	
}
