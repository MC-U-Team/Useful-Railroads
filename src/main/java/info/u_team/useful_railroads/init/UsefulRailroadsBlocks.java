package info.u_team.useful_railroads.init;

import java.util.List;

import info.u_team.u_team_core.util.registry.BaseRegistryUtil;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.block.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.MOD)
public class UsefulRailroadsBlocks {
	
	public static final BlockRailHighSpeed RAIL_HIGHSPEEDRAIL = new BlockRailHighSpeed("rail_highspeed");
	
	// public static final BlockRailTeleport rail_teleport = new
	// BlockRailTeleport("rail_teleport");
	
	public static final BlockRailDirection RAIL_DIRECTION = new BlockRailDirection("rail_direction");
	
	public static final BlockRailIntersection RAIL_INTERSECTION = new BlockRailIntersection("rail_intersection");
	
	@SubscribeEvent
	public static void register(Register<Block> event) {
		ENTRIES = BaseRegistryUtil.getAllRegistryEntriesAndApplyNames(UsefulRailroadsMod.MODID, Block.class);
		ENTRIES.forEach(event.getRegistry()::register);
	}
	
	@SubscribeEvent
	public static void registerBlockItem(Register<Item> event) {
		BaseRegistryUtil.getBlockItems(ENTRIES).forEach(event.getRegistry()::register);
	}
	
	// Just a cache for the block item registry for performance
	private static List<Block> ENTRIES;
}
