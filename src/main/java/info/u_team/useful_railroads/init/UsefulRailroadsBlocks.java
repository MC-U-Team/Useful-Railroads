package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.util.registry.BlockDeferredRegister;
import info.u_team.u_team_core.util.registry.BlockRegistryObject;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.block.BufferStopBlock;
import info.u_team.useful_railroads.block.DirectionRailBlock;
import info.u_team.useful_railroads.block.HighSpeedRailBlock;
import info.u_team.useful_railroads.block.IntersectionRailBlock;
import info.u_team.useful_railroads.block.SpeedClampRailBlock;
import info.u_team.useful_railroads.block.TeleportRailBlock;
import net.minecraft.item.BlockItem;
import net.minecraftforge.eventbus.api.IEventBus;

public class UsefulRailroadsBlocks {
	
	public static final BlockDeferredRegister BLOCKS = BlockDeferredRegister.create(UsefulRailroadsMod.MODID);
	
	public static final BlockRegistryObject<HighSpeedRailBlock, BlockItem> HIGHSPEED_RAIL = BLOCKS.register("highspeed_rail", HighSpeedRailBlock::new);
	public static final BlockRegistryObject<SpeedClampRailBlock, BlockItem> SPEED_CLAMP_RAIL = BLOCKS.register("clamp_rail", SpeedClampRailBlock::new);
	public static final BlockRegistryObject<DirectionRailBlock, BlockItem> DIRECTION_RAIL = BLOCKS.register("direction_rail", DirectionRailBlock::new);
	public static final BlockRegistryObject<IntersectionRailBlock, BlockItem> INTERSECTION_RAIL = BLOCKS.register("intersection_rail", IntersectionRailBlock::new);
	public static final BlockRegistryObject<TeleportRailBlock, BlockItem> TELEPORT_RAIL = BLOCKS.register("teleport_rail", TeleportRailBlock::new);
	
	public static final BlockRegistryObject<BufferStopBlock, BlockItem> BUFFER_STOP = BLOCKS.register("buffer_stop", BufferStopBlock::new);
	
	public static void registerMod(IEventBus bus) {
		BLOCKS.register(bus);
	}
}
