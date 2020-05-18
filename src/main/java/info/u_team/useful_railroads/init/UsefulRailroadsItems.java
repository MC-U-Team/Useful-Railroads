package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.util.registry.BaseRegistryUtil;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.item.StandardGaugeBuilder;
import info.u_team.useful_railroads.item.TrackBuilderItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.MOD)
public class UsefulRailroadsItems {
	
	public static final TrackBuilderItem SINGLE_TRACK_BUILDER = new TrackBuilderItem("single_track_builder", false);
	
	public static final TrackBuilderItem DOUBLE_TRACK_BUILDER = new TrackBuilderItem("double_track_builder", true);
	
	public static final StandardGaugeBuilder STANDARD_TRACK_BUILDER = new StandardGaugeBuilder("sleeper_builder");
	
	@SubscribeEvent
	public static void register(Register<Item> event) {
		BaseRegistryUtil.getAllGenericRegistryEntriesAndApplyNames(UsefulRailroadsMod.MODID, Item.class).forEach(event.getRegistry()::register);
	}
	
}
