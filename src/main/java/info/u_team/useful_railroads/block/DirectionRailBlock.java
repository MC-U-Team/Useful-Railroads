package info.u_team.useful_railroads.block;

import net.minecraft.block.*;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.*;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public class DirectionRailBlock extends CustomPoweredRailBlock {
	
	public static final BooleanProperty AXIS_DIRECTION = BooleanProperty.create("positive_axis");
	
	public DirectionRailBlock(String name) {
		super(name);
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
			public RailState update(boolean powered, boolean placing) {
				boolean hasRailNorth = func_208512_d(pos.north());
				boolean hasRailSouth = func_208512_d(pos.south());
				boolean hasRailWest = func_208512_d(pos.west());
				boolean hasRailEast = func_208512_d(pos.east());
				
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
						RailState railstate = createForAdjacent(connectedRails.get(i));
						if (railstate != null) {
							railstate.checkConnected();
							if (railstate.func_196905_c(this)) {
								railstate.func_208510_c(this);
							}
						}
					}
				}
				return this;
			}
		}).update(world.isBlockPowered(pos), placing).getNewState();
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getPlacementHorizontalFacing();
		return getDefaultState().with(AXIS_DIRECTION, direction.getAxisDirection() == AxisDirection.POSITIVE).with(SHAPE, direction.getAxis() == Axis.Z ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(SHAPE, POWERED, AXIS_DIRECTION);
	}
}
