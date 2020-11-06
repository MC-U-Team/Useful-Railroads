package info.u_team.useful_railroads.block;

import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.useful_railroads.init.UsefulRailroadsTileEntityTypes;
import info.u_team.useful_railroads.tileentity.BufferStopTileEntity;
import info.u_team.useful_railroads.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.*;
import net.minecraft.state.*;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.*;
import net.minecraft.util.*;
import net.minecraft.util.Direction.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraftforge.items.*;

public class BufferStopBlock extends CustomAdvancedTileEntityRailBlock {
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	
	private static final Map<Direction, VoxelShape> VOXEL_SHAPES = createVoxelShape();
	
	private static List<Pair<Vector3d, Vector3d>> createSideShapeVec(int bracketStart, int bracketEnd, int supportStart, int supportEnd, int stopperStart, int stopperEnd) {
		final List<Pair<Vector3d, Vector3d>> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add(VoxelShapeUtil.createVectorPair(bracketStart, 2 + i, 14 - i, bracketEnd, 3 + i, 16 - i));
		}
		list.add(VoxelShapeUtil.createVectorPair(stopperStart, 11, 0, stopperEnd, 16, 1));
		list.add(VoxelShapeUtil.createVectorPair(supportStart, 2, 2, supportEnd, 14, 6));
		return list;
	}
	
	private static Map<Direction, VoxelShape> createVoxelShape() {
		final List<Pair<Vector3d, Vector3d>> northShape = new ArrayList<>();
		northShape.addAll(createSideShapeVec(2, 3, 2, 4, 0, 5));
		northShape.addAll(createSideShapeVec(13, 14, 12, 14, 11, 16));
		northShape.add(VoxelShapeUtil.createVectorPair(0, 12, 1, 16, 15, 3));
		
		return VoxelShapeUtil.getHorizontalRotations(northShape).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, valueEntry -> {
			return VoxelShapes.or(FLAT_AABB, VoxelShapeUtil.createVoxelShapeFromVector(valueEntry.getValue()).stream().toArray(VoxelShape[]::new));
		}));
	}
	
	public BufferStopBlock() {
		super(Properties.create(Material.IRON).doesNotBlockMovement().hardnessAndResistance(1.5F).sound(SoundType.METAL), UsefulRailroadsTileEntityTypes.BUFFER_STOP);
		setDefaultState(getDefaultState().with(SHAPE, RailShape.NORTH_SOUTH).with(FACING, Direction.NORTH).with(POWERED, false));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return VOXEL_SHAPES.getOrDefault(state.get(FACING), VOXEL_SHAPES.get(Direction.NORTH));
	}
	
	@Override
	public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		cart.setMotion(0, 0, 0);
		
		final boolean powered = state.get(POWERED);
		
		if (!powered) {
			final Direction direction = state.get(FACING);
			final Vector3d vec = cart.getPositionVec().add(direction.getXOffset() * 1.1, 0, direction.getZOffset() * 1.1);
			cart.setLocationAndAngles(vec.getX(), vec.getY(), vec.getZ(), cart.rotationYaw, cart.rotationPitch);
		}
		
		if (world.isRemote || !powered) {
			return;
		}
		
		final Optional<BufferStopTileEntity> tileEntityOptional = isTileEntityFromType(world, pos);
		tileEntityOptional.map(BufferStopTileEntity::getMinecartSlots).ifPresent(minecartSlots -> {
			cart.removePassengers();
			
			final Collection<ItemEntity> drops = new ArrayList<>();
			cart.captureDrops(drops);
			cart.killMinecart(DamageSource.MAGIC);
			
			drops.stream().map(ItemEntity::getItem).forEach(stack -> {
				final ItemStack stackLeft = ItemHandlerHelper.insertItem(minecartSlots, stack, false);
				if (!stackLeft.isEmpty()) {
					spawnAsEntity(world, pos, stackLeft);
				}
			});
		});
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		if (context.getEntity() instanceof AbstractMinecartEntity) {
			final Vector3d motion = context.getEntity().getMotion();
			
			final Direction oppositeDirection = state.get(FACING).getOpposite();
			final Axis axis = oppositeDirection.getAxis();
			final AxisDirection axisDirection = oppositeDirection.getAxisDirection();
			
			if (isRightCollision(axis, Axis.X, axisDirection, motion.getX()) || isRightCollision(axis, Axis.Z, axisDirection, motion.getZ())) {
				return VoxelShapes.empty();
			}
		}
		return state.getShape(world, pos);
	}
	
	private final boolean isRightCollision(Axis axis, Axis axisToCheck, AxisDirection axisDirection, double motion) {
		return axis == axisToCheck && Math.abs(motion) > 0.01 && (axisDirection == AxisDirection.NEGATIVE && motion < 0 || axisDirection == AxisDirection.POSITIVE && motion > 0);
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
		} else {
			final Optional<BufferStopTileEntity> tileEntityOptional = isTileEntityFromType(world, pos);
			tileEntityOptional.map(BufferStopTileEntity::getMinecartSlots).ifPresent(minecartSlots -> {
				ItemHandlerUtil.getStackStream(minecartSlots).forEach(stack -> spawnAsEntity(world, pos, stack));
			});
			world.updateComparatorOutputLevel(pos, this);
		}
		super.onReplaced(state, world, pos, newState, isMoving);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		updatePower(world, state, pos);
	}
	
	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		super.neighborChanged(state, world, pos, block, fromPos, isMoving);
		updatePower(world, state, pos);
	}
	
	private void updatePower(World world, BlockState state, BlockPos pos) {
		final boolean powered = isPowered(world, pos);
		if (powered != state.get(POWERED)) {
			world.setBlockState(pos, state.with(POWERED, powered));
		}
	}
	
	private boolean isPowered(World world, BlockPos pos) {
		return Stream.of(Direction.values()).anyMatch(direction -> isPowered(world, pos, direction));
	}
	
	private boolean isPowered(World world, BlockPos pos, Direction direction) {
		final BlockPos relativePos = pos.offset(direction);
		final int value = world.getRedstonePower(relativePos, direction);
		if (value >= 15) {
			return true;
		} else {
			final BlockState relativeState = world.getBlockState(relativePos);
			return Math.max(value, relativeState.getBlock() == Blocks.REDSTONE_WIRE ? relativeState.get(RedstoneWireBlock.POWER) : 0) > 0;
		}
	}
	
	@Override
	public boolean canProvidePower(BlockState state) {
		return true;
	}
	
	@Override
	public float getRailMaxSpeed(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		return 0;
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(SHAPE, FACING, POWERED);
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
