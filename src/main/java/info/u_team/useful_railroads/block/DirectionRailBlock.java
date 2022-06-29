package info.u_team.useful_railroads.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.RailState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.material.Fluids;

public class DirectionRailBlock extends CustomPoweredRailBlock {
	
	public static final BooleanProperty AXIS_DIRECTION = BooleanProperty.create("positive_axis");
	
	public DirectionRailBlock() {
		registerDefaultState(defaultBlockState().setValue(AXIS_DIRECTION, false));
	}
	
	@Override
	public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
		if (!state.getValue(PoweredRailBlock.POWERED)) {
			return;
		}
		
		final RailShape shape = getRailDirection(state, level, pos, cart);
		final boolean positiveAxis = state.getValue(AXIS_DIRECTION);
		
		if (shape == RailShape.EAST_WEST) {
			cart.setDeltaMovement(positiveAxis ? 0.6 : -0.6, cart.getDeltaMovement().y(), cart.getDeltaMovement().z());
		} else if (shape == RailShape.NORTH_SOUTH) {
			cart.setDeltaMovement(cart.getDeltaMovement().x(), cart.getDeltaMovement().y(), positiveAxis ? 0.6 : -0.6);
		}
	}
	
	@Override
	public boolean canMakeSlopes(BlockState state, BlockGetter level, BlockPos pos) {
		return false;
	}
	
	@Override
	protected BlockState updateDir(Level level, BlockPos pos, BlockState updateState, boolean placing) {
		return level.isClientSide ? updateState : (new RailState(level, pos, updateState) {
			
			@Override
			public RailState place(boolean powered, boolean placing, RailShape unused) {
				final boolean hasRailNorth = hasNeighborRail(pos.north());
				final boolean hasRailSouth = hasNeighborRail(pos.south());
				final boolean hasRailWest = hasNeighborRail(pos.west());
				final boolean hasRailEast = hasNeighborRail(pos.east());
				
				RailShape shape = null;
				
				if ((hasRailNorth || hasRailSouth) && !hasRailWest && !hasRailEast) {
					shape = RailShape.NORTH_SOUTH;
				}
				
				if ((hasRailWest || hasRailEast) && !hasRailNorth && !hasRailSouth) {
					shape = RailShape.EAST_WEST;
				}
				
				if (shape == null) {
					shape = state.getValue(SHAPE);
				}
				
				updateConnections(shape);
				state = state.setValue(SHAPE, shape);
				
				if (placing || level.getBlockState(pos) != state) {
					level.setBlock(pos, state, 3);
					
					for (final BlockPos connection : connections) {
						final RailState railstate = getRail(connection);
						if (railstate != null) {
							railstate.removeSoftConnections();
							if (railstate.canConnectTo(this)) {
								railstate.connectTo(this);
							}
						}
					}
				}
				return this;
			}
		}).place(level.hasNeighborSignal(pos), placing, null).getState();
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		final Direction direction = context.getHorizontalDirection();
		final boolean isWater = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
		return defaultBlockState().setValue(AXIS_DIRECTION, direction.getAxisDirection() == AxisDirection.POSITIVE).setValue(SHAPE, direction.getAxis() == Axis.Z ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST).setValue(WATERLOGGED, isWater);
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(AXIS_DIRECTION);
	}
}
