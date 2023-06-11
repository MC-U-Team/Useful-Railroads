package info.u_team.useful_railroads.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.useful_railroads.blockentity.BufferStopBlockEntity;
import info.u_team.useful_railroads.init.UsefulRailroadsBlockEntityTypes;
import info.u_team.useful_railroads.util.ItemHandlerUtil;
import info.u_team.useful_railroads.util.VoxelShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;

public class BufferStopBlock extends CustomAdvancedBlockEntityRailBlock {
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	
	private static final Map<Direction, VoxelShape> VOXEL_SHAPES = createVoxelShape();
	
	private static List<Pair<Vec3, Vec3>> createSideShapeVec(int bracketStart, int bracketEnd, int supportStart, int supportEnd, int stopperStart, int stopperEnd) {
		final List<Pair<Vec3, Vec3>> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add(VoxelShapeUtil.createVectorPair(bracketStart, 2 + i, 14 - i, bracketEnd, 3 + i, 16 - i));
		}
		list.add(VoxelShapeUtil.createVectorPair(stopperStart, 11, 0, stopperEnd, 16, 1));
		list.add(VoxelShapeUtil.createVectorPair(supportStart, 2, 2, supportEnd, 14, 6));
		return list;
	}
	
	private static Map<Direction, VoxelShape> createVoxelShape() {
		final List<Pair<Vec3, Vec3>> northShape = new ArrayList<>();
		northShape.addAll(createSideShapeVec(2, 3, 2, 4, 0, 5));
		northShape.addAll(createSideShapeVec(13, 14, 12, 14, 11, 16));
		northShape.add(VoxelShapeUtil.createVectorPair(0, 12, 1, 16, 15, 3));
		
		return VoxelShapeUtil.getHorizontalRotations(northShape).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, valueEntry -> {
			return Shapes.or(FLAT_AABB, VoxelShapeUtil.createVoxelShapeFromVector(valueEntry.getValue()).stream().toArray(VoxelShape[]::new));
		}));
	}
	
	public BufferStopBlock() {
		super(Properties.of().mapColor(MapColor.COLOR_YELLOW).noCollission().strength(1.5F).sound(SoundType.METAL), UsefulRailroadsBlockEntityTypes.BUFFER_STOP);
		registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return VOXEL_SHAPES.getOrDefault(state.getValue(FACING), VOXEL_SHAPES.get(Direction.NORTH));
	}
	
	@Override
	public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
		cart.setDeltaMovement(0, 0, 0);
		
		final boolean powered = state.getValue(POWERED);
		
		if (!powered) {
			final Direction direction = state.getValue(FACING);
			final Vec3 vec = cart.position().add(direction.getStepX() * 1.1, 0, direction.getStepZ() * 1.1);
			cart.moveTo(vec.x(), vec.y(), vec.z(), cart.getYRot(), cart.getXRot());
		}
		
		if (level.isClientSide || !powered) {
			return;
		}
		
		final Optional<BufferStopBlockEntity> tileEntityOptional = getBlockEntity(level, pos);
		tileEntityOptional.ifPresent(bufferStop -> {
			cart.ejectPassengers();
			
			final Collection<ItemEntity> drops = new ArrayList<>();
			cart.captureDrops(drops);
			cart.destroy(cart.damageSources().magic());
			
			drops.stream().map(ItemEntity::getItem).forEach(stack -> {
				final ItemStack stackLeft = ItemHandlerHelper.insertItem(bufferStop.getMinecartSlots(), stack, false);
				if (!stackLeft.isEmpty()) {
					popResource(level, pos, stackLeft);
				}
				bufferStop.setChanged();
			});
		});
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (context instanceof final EntityCollisionContext entityContext && entityContext.getEntity() instanceof final AbstractMinecart cart) {
			final Vec3 motion = cart.getDeltaMovement();
			
			final Direction oppositeDirection = state.getValue(FACING).getOpposite();
			final Axis axis = oppositeDirection.getAxis();
			final AxisDirection axisDirection = oppositeDirection.getAxisDirection();
			
			if (isRightCollision(axis, Axis.X, axisDirection, motion.x()) || isRightCollision(axis, Axis.Z, axisDirection, motion.z())) {
				return Shapes.empty();
			}
		}
		return state.getShape(level, pos);
		
	}
	
	private final boolean isRightCollision(Axis axis, Axis axisToCheck, AxisDirection axisDirection, double motion) {
		return axis == axisToCheck && Math.abs(motion) > 0.01 && (axisDirection == AxisDirection.NEGATIVE && motion < 0 || axisDirection == AxisDirection.POSITIVE && motion > 0);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		final Direction direction = context.getHorizontalDirection().getOpposite();
		final boolean isWater = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
		return defaultBlockState().setValue(SHAPE, direction.getAxis() == Axis.Z ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST).setValue(FACING, direction).setValue(WATERLOGGED, isWater);
	}
	
	@Override
	protected BlockState updateDir(Level level, BlockPos pos, BlockState state, boolean placing) {
		return state;
	}
	
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() == newState.getBlock()) {
			final RailShape newShape = newState.getValue(SHAPE);
			final Direction newDirection = newState.getValue(FACING);
			if (newShape != state.getValue(SHAPE)) {
				if (newDirection.getAxis() == Axis.Z && newShape != RailShape.NORTH_SOUTH) {
					level.setBlockAndUpdate(pos, newState.setValue(SHAPE, RailShape.NORTH_SOUTH));
				} else if (newDirection.getAxis() == Axis.X && newShape != RailShape.EAST_WEST) {
					level.setBlockAndUpdate(pos, newState.setValue(SHAPE, RailShape.EAST_WEST));
				}
			}
		} else {
			final Optional<BufferStopBlockEntity> tileEntityOptional = getBlockEntity(level, pos);
			tileEntityOptional.map(BufferStopBlockEntity::getMinecartSlots).ifPresent(minecartSlots -> {
				ItemHandlerUtil.getStackStream(minecartSlots).forEach(stack -> popResource(level, pos, stack));
			});
			level.updateNeighbourForOutputSignal(pos, this);
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}
	
	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		updatePower(level, state, pos);
	}
	
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		super.neighborChanged(state, level, pos, block, fromPos, isMoving);
		updatePower(level, state, pos);
	}
	
	private void updatePower(Level level, BlockState state, BlockPos pos) {
		final boolean powered = isPowered(level, pos);
		if (powered != state.getValue(POWERED)) {
			level.setBlockAndUpdate(pos, state.setValue(POWERED, powered));
		}
	}
	
	private boolean isPowered(Level level, BlockPos pos) {
		return Stream.of(Direction.values()).anyMatch(direction -> isPowered(level, pos, direction));
	}
	
	private boolean isPowered(Level level, BlockPos pos, Direction direction) {
		final BlockPos relativePos = pos.relative(direction);
		final int value = level.getSignal(relativePos, direction);
		if (value >= 15) {
			return true;
		} else {
			final BlockState relativeState = level.getBlockState(relativePos);
			return Math.max(value, relativeState.getBlock() == Blocks.REDSTONE_WIRE ? relativeState.getValue(RedStoneWireBlock.POWER) : 0) > 0;
		}
	}
	
	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}
	
	@Override
	public float getRailMaxSpeed(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
		return 0;
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, POWERED);
	}
	
	@Override
	public boolean canMakeSlopes(BlockState state, BlockGetter level, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isFlexibleRail(BlockState state, BlockGetter level, BlockPos pos) {
		return false;
	}
	
}
