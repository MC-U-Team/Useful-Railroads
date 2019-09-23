package info.u_team.useful_railroads.util;

import java.util.*;
import java.util.stream.Stream;

import info.u_team.useful_railroads.inventory.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TrackBuilderManager {
	
	private final World world;
	private final Direction direction;
	private final BlockPos startPos;
	
	private final Set<BlockPos> allPositionSet = new HashSet<>();
	
	private final Set<BlockPos> railSet = new HashSet<>();
	private final Set<BlockPos> groundBlockSet = new HashSet<>();
	private final Set<BlockPos> redstoneTorchSet = new HashSet<>();
	private final Set<BlockPos> cobbleSet = new HashSet<>();
	private final Set<BlockPos> airSet = new HashSet<>();
	
	public TrackBuilderManager(BlockPos rayTracePos, Direction rayTraceFace, World world, Vec3d lookVector) {
		this.world = world;
		direction = Direction.getFacingFromVector(lookVector.x, lookVector.y, lookVector.z);
		
		if (rayTraceFace.getAxis().isHorizontal()) {
			rayTracePos = rayTracePos.offset(direction.getOpposite());
		}
		if (!world.getBlockState(rayTracePos).isSolid()) {
			rayTracePos = rayTracePos.down();
		}
		startPos = rayTracePos.toImmutable();
	}
	
	public boolean calculateBlockPosition() {
		if (direction.getAxis().isVertical()) {
			return false;
		}
		
		final Axis crossWiseAxis = direction.rotateY().getAxis();
		
		final Direction crossWisePositiveDirection = Direction.getFacingFromAxisDirection(crossWiseAxis, AxisDirection.POSITIVE);
		final Direction crossWiseNegativeDirection = Direction.getFacingFromAxisDirection(crossWiseAxis, AxisDirection.NEGATIVE);
		
		BlockPos.getAllInBox(startPos.offset(direction).up(), startPos.offset(direction, 16).up()) //
				.map(BlockPos::toImmutable) //
				.forEach(railSet::add);
		
		BlockPos.getAllInBox(startPos.offset(direction).offset(crossWisePositiveDirection), startPos.offset(direction, 16).offset(crossWiseNegativeDirection)) //
				.map(BlockPos::toImmutable) //
				.forEach(groundBlockSet::add);
		
		redstoneTorchSet.add(startPos.offset(direction, 8).down().toImmutable());
		
		BlockPos.getAllInBox(startPos.offset(direction).offset(crossWisePositiveDirection).down(), startPos.offset(direction, 16).offset(crossWiseNegativeDirection).down(2)) //
				.map(BlockPos::toImmutable) //
				.filter(Predicates.not(redstoneTorchSet::contains)) //
				.forEach(cobbleSet::add);
		
		BlockPos.getAllInBox(startPos.offset(direction).offset(crossWisePositiveDirection).up(1), startPos.offset(direction, 16).offset(crossWiseNegativeDirection).up(3)) //
				.map(BlockPos::toImmutable) //
				.filter(Predicates.not(railSet::contains)) //
				.forEach(airSet::add);
		
		Stream.of(railSet, groundBlockSet, redstoneTorchSet, cobbleSet, airSet).flatMap(Set::stream).forEach(allPositionSet::add);
		
		return true;
	}
	
	public void execute(PlayerEntity player, TrackBuilderInventoryWrapper wrapper) {
		if (world.isRemote || !(world instanceof ServerWorld)) {
			return;
		}
		
		final int cost = calculateCost();
		if (wrapper.getFuel() < cost) {
			player.sendMessage(new StringTextComponent("Not enough fuel."));
			return;
		}
		
		if (!hasEnoughItems(wrapper.getRailInventory(), railSet) || !hasEnoughItems(wrapper.getGroundBlockInventory(), groundBlockSet) || !hasEnoughItems(wrapper.getRedstoneTorchInventory(), redstoneTorchSet)) {
			player.sendMessage(new StringTextComponent("Missing blocks."));
			return;
		}
		
		wrapper.setFuel(wrapper.getFuel() - cost);
		
		allPositionSet.stream().filter(Predicates.not(world::isAirBlock)).forEach(pos -> {
			destroyBlock(pos);
		});
		
		cobbleSet.forEach(pos -> placeBlock(pos, Blocks.COBBLESTONE.getDefaultState()));
		
		wrapper.writeItemStack();
	}
	
	private boolean placeBlock(BlockPos pos, BlockState state) {
		return placeBlock(pos, state, 3);
	}
	
	private boolean placeBlock(BlockPos pos, BlockState state, int flag) {
		final boolean blockSnapshotValue = world.captureBlockSnapshots;
		world.captureBlockSnapshots = false; // Disable capture block snapshots here because else the client will not receive updates
		final boolean placed = world.setBlockState(pos, state, flag);
		world.captureBlockSnapshots = blockSnapshotValue;
		return placed;
	}
	
	private void destroyBlock(BlockPos pos) {
		final BlockState state = world.getBlockState(pos);
		final int exp = state.getExpDrop(world, pos, 0, 0);
		
		if (placeBlock(pos, world.getFluidState(pos).getBlockState())) {
			Block.spawnDrops(state, world, startPos, world.getTileEntity(pos)); // Drop items on start pos
			if (exp > 0) {
				state.getBlock().dropXpOnBlockBreak(world, startPos, exp); // Drop exp on start pos
			}
		}
	}
	
	private boolean hasEnoughItems(BlockTagItemStackHandler handler, Set<BlockPos> set) {
		return ItemHandlerUtil.getItemCount(handler, handler::getCondition) >= set.size();
	}
	
	private int calculateCost() {
		int breakCount = (int) allPositionSet.stream().filter(Predicates.not(world::isAirBlock)).count();
		int placeCount = allPositionSet.size() - airSet.size();
		
		return breakCount * 2 + placeCount;
	}
	
	public Set<BlockPos> getAllPositionsSet() {
		return allPositionSet;
	}
}
