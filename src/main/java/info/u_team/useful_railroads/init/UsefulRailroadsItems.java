package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.util.registry.CommonDeferredRegister;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.item.TrackBuilderItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class UsefulRailroadsItems {
	
	public static final CommonDeferredRegister<Item> ITEMS = CommonDeferredRegister.create(ForgeRegistries.ITEMS, UsefulRailroadsMod.MODID);
	
	public static final RegistryObject<TrackBuilderItem> SINGLE_TRACK_BUILDER = ITEMS.register("single_track_builder", () -> new TrackBuilderItem(false));
	
	public static final RegistryObject<TrackBuilderItem> DOUBLE_TRACK_BUILDER = ITEMS.register("double_track_builder", () -> new TrackBuilderItem(true));
	
	public static void register(IEventBus bus) {
		ITEMS.register(bus);
	}
	
}
