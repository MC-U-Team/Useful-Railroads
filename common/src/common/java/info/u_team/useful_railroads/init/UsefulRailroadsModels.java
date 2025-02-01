package info.u_team.useful_railroads.init;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.DIRECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.HIGHSPEED_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.SPEED_CLAMP_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.TELEPORT_RAIL;

import info.u_team.u_team_core.api.event.CommonEvents;
import info.u_team.u_team_core.util.ModelUtil;
import info.u_team.useful_railroads.block.RailBlockCustomProperty;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RailShape;

public class UsefulRailroadsModels {
	
	private static void onSetup() {
		System.out.println("______________________________________________________________________________ REGISTERD");
		ModelUtil.addCustomStateContainer(HIGHSPEED_RAIL.getId(), (new StateDefinition.Builder<Block, BlockState>(HIGHSPEED_RAIL.get())).add(BaseRailBlock.WATERLOGGED, PoweredRailBlock.POWERED, EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST)).create(Block::defaultBlockState, BlockState::new));
		ModelUtil.addCustomStateContainer(SPEED_CLAMP_RAIL.getId(), (new StateDefinition.Builder<Block, BlockState>(SPEED_CLAMP_RAIL.get())).add(BaseRailBlock.WATERLOGGED, PoweredRailBlock.POWERED, EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST)).create(Block::defaultBlockState, BlockState::new));
		ModelUtil.addCustomStateContainer(DIRECTION_RAIL.getId(), (new StateDefinition.Builder<Block, BlockState>(DIRECTION_RAIL.get())).add(BaseRailBlock.WATERLOGGED, PoweredRailBlock.POWERED, EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST)).add(RailBlockCustomProperty.AXIS_DIRECTION).create(Block::defaultBlockState, BlockState::new));
		ModelUtil.addCustomStateContainer(TELEPORT_RAIL.getId(), (new StateDefinition.Builder<Block, BlockState>(TELEPORT_RAIL.get())).add(BaseRailBlock.WATERLOGGED, PoweredRailBlock.POWERED, EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST)).create(Block::defaultBlockState, BlockState::new));
	}
	
	static void register() {
		CommonEvents.registerSetup(UsefulRailroadsModels::onSetup, true);
	}
	
}
