package info.u_team.useful_railroads.block;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.useful_railroads.init.UsefulRailroadsTileEntityTypes;
import info.u_team.useful_railroads.util.VoxelShapeUtil;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.*;
import net.minecraft.util.*;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.*;

public class BufferStopBlock extends CustomAdvancedTileEntityRailBlock {
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	
	private static final Map<Direction, VoxelShape> VOXEL_SHAPES = createVoxelShape();
	
	private static List<Pair<Vec3d, Vec3d>> createSideShapeVec(int bracketStart, int bracketEnd, int supportStart, int supportEnd, int stopperStart, int stopperEnd) {
		final List<Pair<Vec3d, Vec3d>> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add(VoxelShapeUtil.createVectorPair(bracketStart, 2 + i, 14 - i, bracketEnd, 3 + i, 16 - i));
		}
		list.add(VoxelShapeUtil.createVectorPair(stopperStart, 11, 0, stopperEnd, 16, 1));
		list.add(VoxelShapeUtil.createVectorPair(supportStart, 2, 2, supportEnd, 14, 6));
		return list;
	}
	
	private static Map<Direction, VoxelShape> createVoxelShape() {
		final List<Pair<Vec3d, Vec3d>> northShape = new ArrayList<>();
		northShape.addAll(createSideShapeVec(2, 3, 2, 4, 0, 5));
		northShape.addAll(createSideShapeVec(13, 14, 12, 14, 11, 16));
		northShape.add(VoxelShapeUtil.createVectorPair(0, 12, 1, 16, 15, 3));
		
		return VoxelShapeUtil.getHorizontalRotations(northShape).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, valueEntry -> {
			return VoxelShapes.or(FLAT_AABB, VoxelShapeUtil.createVoxelShapeFromVector(valueEntry.getValue()).stream().toArray(VoxelShape[]::new));
		}));
	}
	
	public BufferStopBlock(String name) {
		super(name, Properties.create(Material.IRON).doesNotBlockMovement().hardnessAndResistance(1.5F).sound(SoundType.METAL), () -> UsefulRailroadsTileEntityTypes.BUFFER_STOP);
		setDefaultState(getDefaultState().with(SHAPE, RailShape.NORTH_SOUTH).with(FACING, Direction.NORTH));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return VOXEL_SHAPES.getOrDefault(state.get(FACING), VOXEL_SHAPES.get(Direction.NORTH));
	}
	
	@Override
	public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		cart.setMotion(0, 0, 0);
		cart.removePassengers();
		cart.remove();
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		final Direction direction = context.getPlacementHorizontalFacing().getOpposite();
		return getDefaultState().with(SHAPE, direction.getAxis() == Axis.Z ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST).with(FACING, direction);
	}
	
	@Override
	protected BlockState getUpdatedState(World worldIn, BlockPos pos, BlockState state, boolean placing) {
		return state;
	}
	
	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() == newState.getBlock()) {
			final RailShape newShape = newState.get(SHAPE);
			final Direction newDirection = newState.get(FACING);
			if (newShape != state.get(SHAPE)) {
				if (newDirection.getAxis() == Axis.Z && newShape != RailShape.NORTH_SOUTH) {
					world.setBlockState(pos, newState.with(SHAPE, RailShape.NORTH_SOUTH));
				} else if (newDirection.getAxis() == Axis.X && newShape != RailShape.EAST_WEST) {
					world.setBlockState(pos, newState.with(SHAPE, RailShape.EAST_WEST));
				}
			}
		}
		super.onReplaced(state, world, pos, newState, isMoving);
	}
	
	@Override
	public float getRailMaxSpeed(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		return 0;
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(SHAPE, FACING);
	}
	
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isFlexibleRail(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
	
}
