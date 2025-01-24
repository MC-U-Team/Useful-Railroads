package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.registry.BlockRegister;
import info.u_team.u_team_core.api.registry.BlockRegistryEntry;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.block.BufferStopBlock;
import info.u_team.useful_railroads.block.DirectionRailBlock;
import info.u_team.useful_railroads.block.HighSpeedRailBlock;
import info.u_team.useful_railroads.block.IntersectionRailBlock;
import info.u_team.useful_railroads.block.SpeedClampRailBlock;
import info.u_team.useful_railroads.block.TeleportRailBlock;
import net.minecraft.world.item.BlockItem;

public class UsefulRailroadsBlocks {
	
	public static final BlockRegister BLOCKS = BlockRegister.create(UsefulRailroadsMod.MODID);
	
	public static final BlockRegistryEntry<HighSpeedRailBlock, BlockItem> HIGHSPEED_RAIL = BLOCKS.register("highspeed_rail", HighSpeedRailBlock::new);
	public static final BlockRegistryEntry<SpeedClampRailBlock, BlockItem> SPEED_CLAMP_RAIL = BLOCKS.register("clamp_rail", SpeedClampRailBlock::new);
	public static final BlockRegistryEntry<DirectionRailBlock, BlockItem> DIRECTION_RAIL = BLOCKS.register("direction_rail", DirectionRailBlock::new);
	public static final BlockRegistryEntry<IntersectionRailBlock, BlockItem> INTERSECTION_RAIL = BLOCKS.register("intersection_rail", IntersectionRailBlock::new);
	public static final BlockRegistryEntry<TeleportRailBlock, BlockItem> TELEPORT_RAIL = BLOCKS.register("teleport_rail", TeleportRailBlock::new);
	
	public static final BlockRegistryEntry<BufferStopBlock, BlockItem> BUFFER_STOP = BLOCKS.register("buffer_stop", BufferStopBlock::new);
	
	static void register() {
		BLOCKS.register();
	}
}
