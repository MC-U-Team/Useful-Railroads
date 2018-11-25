package info.u_team.useful_railroads.event;

import info.u_team.useful_railroads.block.BlockCustomRailPowered;
import net.minecraft.block.*;
import net.minecraft.block.BlockRailBase.EnumRailDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerNeighborNotify {
	
	// Just minecraft rail logic stuff, nearly all copied
	
	@SubscribeEvent
	public static void on(NeighborNotifyEvent event) {
		IBlockState state = event.getState();
		Block block = state.getBlock();
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		
		if (!(block instanceof BlockRailPowered)) {
			return;
		}
		
		BlockRailPowered rail = (BlockRailPowered) block;
		
		if (!world.isRemote) {
			EnumRailDirection direction = rail.getRailDirection(world, pos, world.getBlockState(pos), null);
			boolean flag = false;
			
			if (!world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP)) {
				flag = true;
			}
			
			if (direction == EnumRailDirection.ASCENDING_EAST && !world.getBlockState(pos.east()).isSideSolid(world, pos.east(), EnumFacing.UP)) {
				flag = true;
			} else if (direction == EnumRailDirection.ASCENDING_WEST && !world.getBlockState(pos.west()).isSideSolid(world, pos.west(), EnumFacing.UP)) {
				flag = true;
			} else if (direction == EnumRailDirection.ASCENDING_NORTH && !world.getBlockState(pos.north()).isSideSolid(world, pos.north(), EnumFacing.UP)) {
				flag = true;
			} else if (direction == EnumRailDirection.ASCENDING_SOUTH && !world.getBlockState(pos.south()).isSideSolid(world, pos.south(), EnumFacing.UP)) {
				flag = true;
			}
			
			if (flag && !world.isAirBlock(pos)) {
				rail.dropBlockAsItem(world, pos, state, 0);
				world.setBlockToAir(pos);
			} else {
				updateState(state, world, pos, rail);
			}
		}
	}
	
	protected static boolean findPoweredRailSignal(World world, BlockPos pos, IBlockState state, boolean positive_axis, int distance) {
		if (distance >= 8) {
			return false;
		} else {
			int i = pos.getX();
			int j = pos.getY();
			int k = pos.getZ();
			boolean flag = true;
			
			Block block = state.getBlock();
			
			EnumRailDirection direction;
			if (block instanceof BlockRailPowered) {
				direction = state.getValue(BlockRailPowered.SHAPE);
			} else {
				direction = state.getValue(BlockCustomRailPowered.SHAPE);
			}
			
			switch (direction) {
			case NORTH_SOUTH:
				
				if (positive_axis) {
					++k;
				} else {
					--k;
				}
				
				break;
			case EAST_WEST:
				
				if (positive_axis) {
					--i;
				} else {
					++i;
				}
				
				break;
			case ASCENDING_EAST:
				
				if (positive_axis) {
					--i;
				} else {
					++i;
					++j;
					flag = false;
				}
				
				direction = EnumRailDirection.EAST_WEST;
				break;
			case ASCENDING_WEST:
				
				if (positive_axis) {
					--i;
					++j;
					flag = false;
				} else {
					++i;
				}
				
				direction = EnumRailDirection.EAST_WEST;
				break;
			case ASCENDING_NORTH:
				
				if (positive_axis) {
					++k;
				} else {
					--k;
					++j;
					flag = false;
				}
				
				direction = EnumRailDirection.NORTH_SOUTH;
				break;
			case ASCENDING_SOUTH:
				
				if (positive_axis) {
					++k;
					++j;
					flag = false;
				} else {
					--k;
				}
				
				direction = EnumRailDirection.NORTH_SOUTH;
			default:
				break;
			}
			
			if (isSameRailWithPower(world, new BlockPos(i, j, k), positive_axis, distance, direction)) {
				return true;
			} else {
				return flag && isSameRailWithPower(world, new BlockPos(i, j - 1, k), positive_axis, distance, direction);
			}
		}
	}
	
	protected static boolean isSameRailWithPower(World world, BlockPos pos, boolean p_176567_3_, int distance, EnumRailDirection direction) {
		IBlockState iblockstate = world.getBlockState(pos);
		Block block = iblockstate.getBlock();
		
		if (block instanceof BlockCustomRailPowered) {
			
			EnumRailDirection directionOther;
			if (block instanceof BlockRailPowered) {
				directionOther = iblockstate.getValue(BlockRailPowered.SHAPE);
			} else {
				directionOther = iblockstate.getValue(BlockCustomRailPowered.SHAPE);
			}
			
			if (direction != EnumRailDirection.EAST_WEST || directionOther != EnumRailDirection.NORTH_SOUTH && directionOther != EnumRailDirection.ASCENDING_NORTH && directionOther != EnumRailDirection.ASCENDING_SOUTH) {
				if (direction != EnumRailDirection.NORTH_SOUTH || directionOther != EnumRailDirection.EAST_WEST && directionOther != EnumRailDirection.ASCENDING_EAST && directionOther != EnumRailDirection.ASCENDING_WEST) {
					
					boolean powered;
					if (block instanceof BlockRailPowered) {
						powered = iblockstate.getValue(BlockRailPowered.POWERED);
					} else {
						powered = iblockstate.getValue(BlockCustomRailPowered.POWERED);
					}
					
					if (powered) {
						return world.isBlockPowered(pos) ? true : findPoweredRailSignal(world, pos, iblockstate, p_176567_3_, distance + 1);
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		return false;
	}
	
	protected static void updateState(IBlockState state, World world, BlockPos pos, Block block) {
		boolean powered;
		if (block instanceof BlockRailPowered) {
			powered = state.getValue(BlockRailPowered.POWERED);
		} else {
			powered = state.getValue(BlockCustomRailPowered.POWERED);
		}
		
		boolean flag = powered;
		boolean flag1 = world.isBlockPowered(pos) || findPoweredRailSignal(world, pos, state, true, 0) || findPoweredRailSignal(world, pos, state, false, 0);
		
		if (flag1 != flag) {
			if (block instanceof BlockRailPowered) {
				world.setBlockState(pos, state.withProperty(BlockRailPowered.POWERED, flag1), 3);
			} else {
				world.setBlockState(pos, state.withProperty(BlockCustomRailPowered.POWERED, flag1), 3);
			}
			world.notifyNeighborsOfStateChange(pos.down(), block, false);
			
			if (block instanceof BlockRailPowered) {
				if (state.getValue(BlockRailPowered.SHAPE).isAscending()) {
					world.notifyNeighborsOfStateChange(pos.up(), block, false);
				}
			} else {
				if (state.getValue(BlockCustomRailPowered.SHAPE).isAscending()) {
					world.notifyNeighborsOfStateChange(pos.up(), block, false);
				}
			}
		}
	}
}
