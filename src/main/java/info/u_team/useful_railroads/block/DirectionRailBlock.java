package info.u_team.useful_railroads.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.RailState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Direction.AxisDirection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class DirectionRailBlock extends CustomPoweredRailBlock {
	
	public static final BooleanProperty AXIS_DIRECTION = BooleanProperty.create("positive_axis");
	
	public DirectionRailBlock() {
		setDefaultState(getDefaultState().with(SHAPE, RailShape.NORTH_SOUTH).with(POWERED, false).with(AXIS_DIRECTION, false));
	}
	
	@Override
	public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		if (!state.get(PoweredRailBlock.POWERED)) {
			return;
		}
		
		final RailShape shape = getRailDirection(state, world, pos, cart);
		final boolean positiveAxis = state.get(AXIS_DIRECTION);
		
		if (shape == RailShape.EAST_WEST) {
			cart.setMotion(positiveAxis ? 0.6 : -0.6, cart.getMotion().getY(), cart.getMotion().getZ());
		} else if (shape == RailShape.NORTH_SOUTH) {
			cart.setMotion(cart.getMotion().getX(), cart.getMotion().getY(), positiveAxis ? 0.6 : -0.6);
		}
	}
	
	@Override
	public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
	
	@Override
	protected BlockState getUpdatedState(World world, BlockPos pos, BlockState state, boolean placing) {
		return world.isRemote ? state : (new RailState(world, pos, state) {
			
			@Override
			public RailState placeRail(boolean powered, boolean placing, RailShape unused) {
				final boolean hasRailNorth = canConnect(pos.north());
				final boolean hasRailSouth = canConnect(pos.south());
				final boolean hasRailWest = canConnect(pos.west());
				final boolean hasRailEast = canConnect(pos.east());
				
				RailShape shape = null;
				
				if ((hasRailNorth || hasRailSouth) && !hasRailWest && !hasRailEast) {
					shape = RailShape.NORTH_SOUTH;
				}
				
				if ((hasRailWest || hasRailEast) && !hasRailNorth && !hasRailSouth) {
					shape = RailShape.EAST_WEST;
				}
				
				if (shape == null) {
					shape = newState.get(SHAPE);
				}
				
				reset(shape);
				newState = newState.with(SHAPE, shape);
				
				if (placing || world.getBlockState(pos) != newState) {
					world.setBlockState(pos, newState, 3);
					
					for (int i = 0; i < connectedRails.size(); ++i) {
						final RailState railstate = createForAdjacent(connectedRails.get(i));
						if (railstate != null) {
							railstate.checkConnected();
							if (railstate.canConnect(this)) {
								railstate.connect(this);
							}
						}
					}
				}
				return this;
			}
		}).placeRail(world.isBlockPowered(pos), placing, null).getNewState();
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		final Direction direction = context.getPlacementHorizontalFacing();
		return getDefaultState().with(AXIS_DIRECTION, direction.getAxisDirection() == AxisDirection.POSITIVE).with(SHAPE, direction.getAxis() == Axis.Z ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(SHAPE, POWERED, AXIS_DIRECTION);
	}
}
