package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.registry.BlockRegistry;
import info.u_team.u_team_core.util.RegistryUtil;
import info.u_team.useful_railroads.UsefulRailroadsConstants;
import info.u_team.useful_railroads.block.*;
import net.minecraft.block.Block;

public class UsefulRailroadsBlocks {
	
	public static final BlockRailHighSpeed rail_highspeedrail = new BlockRailHighSpeed("rail_highspeed");
	
	public static final BlockRailTeleport rail_teleport = new BlockRailTeleport("rail_teleport");
	
	public static final BlockRailDirection rail_direction = new BlockRailDirection("rail_direction");
	
	public static final BlockRailIntersection rail_intersection = new BlockRailIntersection("rail_intersection");
	
	public static void preinit() {
		BlockRegistry.register(UsefulRailroadsConstants.MODID, RegistryUtil.getRegistryEntries(Block.class, UsefulRailroadsBlocks.class));
	}
	
}
