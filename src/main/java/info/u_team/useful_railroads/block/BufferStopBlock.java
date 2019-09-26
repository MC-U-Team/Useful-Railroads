package info.u_team.useful_railroads.block;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.useful_railroads.init.UsefulRailroadsTileEntityTypes;
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
	
	private VoxelShape createSideShape(int bracketStart, int bracketEnd, int supportStart, int supportEnd, int stopperStart, int stopperEnd) {
		final VoxelShape[] array = new VoxelShape[11];
		for (int i = 0; i < 10; i++) {
			array[i] = Block.makeCuboidShape(14 - i, 2 + i, bracketStart, 16 - i, 3 + i, bracketEnd);
		}
		
		array[10] = Block.makeCuboidShape(0, 11, stopperStart, 1, 16, stopperEnd);
		return VoxelShapes.or(Block.makeCuboidShape(2, 2, supportStart, 6, 14, supportEnd), array);
	}
	
	private List<Pair<Vec3d, Vec3d>> createSideShapeVec(int bracketStart, int bracketEnd, int supportStart, int supportEnd, int stopperStart, int stopperEnd) {
		final List<Pair<Vec3d, Vec3d>> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add(Pair.of(new Vec3d(14 - i, 2 + i, bracketStart), new Vec3d(16 - i, 3 + i, bracketEnd)));
		}
		list.add(Pair.of(new Vec3d(0, 11, stopperStart), new Vec3d(1, 16, stopperEnd)));
		list.add(Pair.of(new Vec3d(2, 2, supportStart), new Vec3d(6, 14, supportEnd)));
		return list;
	}
	
	private List<VoxelShape> createVoxelShapeVector(List<Pair<Vec3d, Vec3d>> list) {
		return list.stream().map(pair -> {
			final Vec3d vec1 = pair.getLeft();
			final Vec3d vec2 = pair.getRight();
			return Block.makeCuboidShape(vec1.getX(), vec1.getY(), vec1.getZ(), vec2.getX(), vec2.getY(), vec2.getZ());
		}).collect(Collectors.toList());
	}
	
	public BufferStopBlock(String name) {
		super(name, Properties.create(Material.IRON).doesNotBlockMovement().hardnessAndResistance(1.5F).sound(SoundType.METAL), () -> UsefulRailroadsTileEntityTypes.BUFFER_STOP);
		setDefaultState(getDefaultState().with(SHAPE, RailShape.NORTH_SOUTH).with(FACING, Direction.NORTH));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		// final VoxelShape west = VoxelShapes.or(FLAT_AABB, Block.makeCuboidShape(1, 12, 0, 3, 15, 16), createSideShape(2, 3,
		// 2, 4, 0, 5), createSideShape(13, 14, 12, 14, 11, 16));
		
		List<Pair<Vec3d, Vec3d>> list = new ArrayList<>();
		
		list.addAll(createSideShapeVec(2, 3, 2, 4, 0, 5));
		list.addAll(createSideShapeVec(13, 14, 12, 14, 11, 16));
		list.add(Pair.of(new Vec3d(1, 12, 0), new Vec3d(3, 15, 16)));
		
		List<Pair<Vec3d, Vec3d>> rotation = list.stream().map(pair -> {
			Vec3d vec1 = pair.getLeft();
			Vec3d vec2 = pair.getRight();
			
			vec1 = vec1.subtract(8, 8, 8);
			vec2 = vec2.subtract(8, 8, 8);
			
			vec1 = rotateVectorCC(vec1, new Vec3d(0, 1, 0), Math.PI * 1.5);
			vec2 = rotateVectorCC(vec2, new Vec3d(0, 1, 0), Math.PI * 1.5);
			
			vec1 = vec1.add(8, 8, 8);
			vec2 = vec2.add(8, 8, 8);
			
			// vec1 = new Vec3d(vec1.getX() + 16, vec1.getY() + 0, vec1.getZ() + 16);
			// vec2 = new Vec3d(vec2.getX() + 16, vec2.getY() + 0, vec2.getZ() + 16);
			
			return Pair.of(vec1, vec2);
		}).collect(Collectors.toList());
		
//		System.out.println("______________________________________________________________________");
//		
//		rotation.forEach(pair -> {
//			Vec3d vec1 = pair.getLeft();
//			Vec3d vec2 = pair.getRight();
//			
//			System.out.println("| " + Math.round(vec1.getX()) + " - " + Math.round(vec1.getY()) + " - " + Math.round(vec1.getZ()) + "|" + "| " + Math.round(vec2.getX()) + " - " + Math.round(vec2.getY()) + " - " + Math.round(vec2.getZ()) + "|");
//		});
//		
//		System.out.println("______________________________________________________________________");
		
		// System.out.println(rotation);
		
		return VoxelShapes.or(FLAT_AABB, createVoxelShapeVector(rotation).stream().toArray(VoxelShape[]::new));
	}
	
	public static Vec3d rotateVectorCC(Vec3d vec, Vec3d axis, double theta) {
		final double x, y, z;
		final double u, v, w;
		x = vec.getX();
		y = vec.getY();
		z = vec.getZ();
		u = axis.getX();
		v = axis.getY();
		w = axis.getZ();
		double xPrime = u * (u * x + v * y + w * z) * (1d - Math.cos(theta)) + x * Math.cos(theta) + (-w * y + v * z) * Math.sin(theta);
		double yPrime = v * (u * x + v * y + w * z) * (1d - Math.cos(theta)) + y * Math.cos(theta) + (w * x - u * z) * Math.sin(theta);
		double zPrime = w * (u * x + v * y + w * z) * (1d - Math.cos(theta)) + z * Math.cos(theta) + (-v * x + u * y) * Math.sin(theta);
		return new Vec3d(xPrime, yPrime, zPrime);
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
