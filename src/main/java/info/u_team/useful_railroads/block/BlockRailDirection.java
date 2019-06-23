package info.u_team.useful_railroads.block;

import net.minecraft.block.*;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRailDirection extends BlockCustomRailPowered {
	
	public static final BooleanProperty AXIS_DIRECTION = BooleanProperty.create("positive_axis");
	
	public BlockRailDirection(String name) {
		super(name);
		setDefaultState(getDefaultState().with(AXIS_DIRECTION, false));
	}
	
	@Override
	public void onMinecartPassPowered(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		final RailShape shape = getRailDirection(state, world, pos, cart);
		
		final boolean positive_axis = state.get(AXIS_DIRECTION);
		
		if (shape == RailShape.EAST_WEST) {
			cart.setMotion(cart.getMotion().add(positive_axis ? 0.6 : -0.6, 0, 0));
		} else if (shape == RailShape.NORTH_SOUTH) {
			cart.setMotion(cart.getMotion().add(0, 0, positive_axis ? 0.6 : -0.6));
		}
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		final Direction placeFacing = context.getPlacementHorizontalFacing();
		return getDefaultState().with(AXIS_DIRECTION, placeFacing.getAxisDirection() == AxisDirection.POSITIVE).with(SHAPE, placeFacing.getAxis() == Axis.Z ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(AXIS_DIRECTION);
	}
	
	@Override
	protected BlockState getUpdatedState(World world, BlockPos pos, BlockState state, boolean placing) {
		return world.isRemote ? state : (new RailState(world, pos, state)).update(world.isBlockPowered(pos), placing).getNewState();
	}
	
	// public class DirectionRail extends Rail {
	//
	// public DirectionRail(World world, BlockPos pos, IBlockState state) {
	// super(world, pos, state);
	// }
	//
	// @Override
	// public Rail place(boolean powered, boolean initialPlacement) {
	// boolean hasRailNorth = hasNeighborRail(pos.north());
	// boolean hasRailSouth = hasNeighborRail(pos.south());
	// boolean hasRailWest = hasNeighborRail(pos.west());
	// boolean hasRailEast = hasNeighborRail(pos.east());
	//
	// EnumRailDirection direction = null;
	// boolean positive_axis = state.getValue(AXIS_DIRECTION);
	//
	// if ((hasRailNorth || hasRailSouth) && !hasRailWest && !hasRailEast) {
	// direction = EnumRailDirection.NORTH_SOUTH;
	// if (hasRailNorth && !hasRailSouth) {
	// positive_axis = false;
	// } else if (hasRailSouth && !hasRailNorth) {
	// positive_axis = true;
	// }
	// }
	//
	// if ((hasRailWest || hasRailEast) && !hasRailNorth && !hasRailSouth) {
	// direction = EnumRailDirection.EAST_WEST;
	// if (hasRailWest && !hasRailEast) {
	// positive_axis = false;
	// } else if (hasRailEast && !hasRailWest) {
	// positive_axis = true;
	// }
	// }
	//
	// if (direction == null) {
	// direction = state.getValue(SHAPE);
	// }
	//
	// updateConnectedRails(direction);
	// state = state.withProperty(SHAPE, direction).withProperty(AXIS_DIRECTION,
	// positive_axis);
	//
	// if (initialPlacement || world.getBlockState(pos) != state) {
	// world.setBlockState(pos, state, 3);
	//
	// for (int i = 0; i < connectedRails.size(); ++i) {
	// Rail rail = findRailAt(connectedRails.get(i));
	//
	// if (rail != null) {
	// rail.removeSoftConnections();
	//
	// if (rail.canConnectTo(this)) {
	// rail.connectTo(this);
	// }
	// }
	// }
	// }
	//
	// return this;
	// }
	// }
}
