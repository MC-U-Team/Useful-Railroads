package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.registry.BlockRegister;
import info.u_team.u_team_core.api.registry.BlockRegistryEntry;
import info.u_team.useful_railroads.UsefulRailroadsReference;
import info.u_team.useful_railroads.block.RailBlockCreator;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class UsefulRailroadsBlocks {
	
	public static final BlockRegister BLOCKS = BlockRegister.create(UsefulRailroadsReference.MODID);
	
	public static final BlockRegistryEntry<? extends Block, BlockItem> HIGHSPEED_RAIL = BLOCKS.register("highspeed_rail", RailBlockCreator::createHighspeedRail);
	public static final BlockRegistryEntry<? extends Block, BlockItem> SPEED_CLAMP_RAIL = BLOCKS.register("clamp_rail", RailBlockCreator::createSpeedClampRail);
	public static final BlockRegistryEntry<? extends Block, BlockItem> DIRECTION_RAIL = BLOCKS.register("direction_rail", RailBlockCreator::createDirectionalRail);
	public static final BlockRegistryEntry<? extends Block, BlockItem> INTERSECTION_RAIL = BLOCKS.register("intersection_rail", RailBlockCreator::createIntersectionRail);
	public static final BlockRegistryEntry<? extends Block, BlockItem> TELEPORT_RAIL = BLOCKS.register("teleport_rail", RailBlockCreator::createTeleportRail);
	
	public static final BlockRegistryEntry<? extends Block, BlockItem> BUFFER_STOP = BLOCKS.register("buffer_stop", RailBlockCreator::createBufferStop);
	
	static void register() {
		BLOCKS.register();
	}
}
