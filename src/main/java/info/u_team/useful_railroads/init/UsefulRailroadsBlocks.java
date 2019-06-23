package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.util.registry.BaseRegistryUtil;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.block.*;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID)
public class UsefulRailroadsBlocks {
	
	public static final BlockRailHighSpeed RAIL_HIGHSPEEDRAIL = new BlockRailHighSpeed("rail_highspeed");
	
	// public static final BlockRailTeleport rail_teleport = new
	// BlockRailTeleport("rail_teleport");
	
	public static final BlockRailDirection RAIL_DIRECTION = new BlockRailDirection("rail_direction");
	
	public static final BlockRailIntersection RAIL_INTERSECTION = new BlockRailIntersection("rail_intersection");
	
	@SubscribeEvent
	public static void register(Register<Block> event) {
		BaseRegistryUtil.getAllRegistryEntriesAndApplyNames(UsefulRailroadsMod.MODID, Block.class).forEach(event.getRegistry()::register);
	}
}
