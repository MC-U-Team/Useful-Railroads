package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.registry.CommonRegister;
import info.u_team.u_team_core.api.registry.RegistryEntry;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.item.TrackBuilderItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class UsefulRailroadsItems {
	
	public static final CommonRegister<Item> ITEMS = CommonRegister.create(Registries.ITEM, UsefulRailroadsMod.MODID);
	
	public static final RegistryEntry<TrackBuilderItem> SINGLE_TRACK_BUILDER = ITEMS.register("single_track_builder", () -> new TrackBuilderItem(false));
	public static final RegistryEntry<TrackBuilderItem> DOUBLE_TRACK_BUILDER = ITEMS.register("double_track_builder", () -> new TrackBuilderItem(true));
	
	static void register() {
		ITEMS.register();
	}
	
}
