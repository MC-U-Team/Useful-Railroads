package info.u_team.useful_railroads.util;

import java.util.*;
import java.util.stream.Stream;

import info.u_team.useful_railroads.inventory.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public abstract class TrackBuilderManager {
	
	protected final World world;
	protected final Direction direction;
	protected final BlockPos startPos;
	
	protected final TrackBuilderMode mode;
	
	protected final Set<BlockPos> allPositionSet = new HashSet<>();
	
	protected final Set<BlockPos> firstRailPos = new HashSet<>();
	protected final Set<BlockPos> railSet = new HashSet<>();
	protected final Set<BlockPos> groundBlockSet = new HashSet<>();
	protected final Set<BlockPos> redstoneTorchSet = new HashSet<>();
	protected final Set<BlockPos> cobbleSet = new HashSet<>();
	protected final Set<BlockPos> airSet = new HashSet<>();
	
	private TrackBuilderManager(BlockPos rayTracePos, Direction rayTraceFace, World world, Vec3d lookVector, TrackBuilderMode mode) {
		this.world = world;
		direction = Direction.getFacingFromVector(lookVector.x, lookVector.y, lookVector.z);
		
		if (rayTraceFace.getAxis().isHorizontal()) {
			rayTracePos = rayTracePos.offset(direction.getOpposite());
		}
		if (!world.getBlockState(rayTracePos).isSolid()) {
			rayTracePos = rayTracePos.down();
		}
		startPos = rayTracePos.toImmutable();
		this.mode = mode;
	}
	
	public void calculateBlockPosition() {
		final Direction directionLeft = direction.rotateYCCW();
		final Direction directionRight = direction.rotateY();
		calculate(directionLeft, directionRight);
		Stream.of(railSet, groundBlockSet, redstoneTorchSet, cobbleSet, airSet).flatMap(Set::stream).forEach(allPositionSet::add);
	}
	
	protected abstract void calculate(Direction directionLeft, Direction directionRight);
	
	public void execute(PlayerEntity player, TrackBuilderInventoryWrapper wrapper) {
		if (world.isRemote || !(world instanceof ServerWorld)) {
			return;
		}
		
		final int cost = calculateCost();
		if (wrapper.getFuel() < cost) {
			player.sendMessage(new TranslationTextComponent("item.usefulrailroads.track_builder.not_enough_fuel", cost).setStyle(new Style().setColor(TextFormatting.RED)));
			return;
		}
		
		if (!hasEnoughItems(wrapper.getRailInventory(), railSet) || !hasEnoughItems(wrapper.getGroundBlockInventory(), groundBlockSet) || !hasEnoughItems(wrapper.getRedstoneTorchInventory(), redstoneTorchSet)) {
			player.sendMessage(new TranslationTextComponent("item.usefulrailroads.track_builder.not_enough_blocks").setStyle(new Style().setColor(TextFormatting.RED)));
			return;
		}
		
		wrapper.setFuel(wrapper.getFuel() - cost);
		
		final List<ItemStack> railStacks = extractItems(wrapper.getRailInventory(), railSet);
		final List<ItemStack> groundBlockStacks = extractItems(wrapper.getGroundBlockInventory(), groundBlockSet);
		final List<ItemStack> redstoneTorchStacks = extractItems(wrapper.getRedstoneTorchInventory(), redstoneTorchSet);
		
		final Inventory dropInventory = new Inventory(50);
		allPositionSet.stream().filter(Predicates.not(world::isAirBlock)).forEach(pos -> destroyBlock(player, pos, dropInventory));
		InventoryHelper.dropInventoryItems(world, player, dropInventory);
		
		cobbleSet.forEach(pos -> placeBlock(pos, Blocks.COBBLESTONE.getDefaultState()));
		redstoneTorchSet.forEach(pos -> placeItemBlock(pos, ItemHandlerUtil.getOneItemAndRemove(redstoneTorchStacks)));
		groundBlockSet.forEach(pos -> placeItemBlock(pos, ItemHandlerUtil.getOneItemAndRemove(groundBlockStacks)));
		railSet.forEach(pos -> placeItemBlock(pos, ItemHandlerUtil.getOneItemAndRemove(railStacks)));
		
		wrapper.writeItemStack();
	}
	
	private void placeItemBlock(BlockPos pos, ItemStack stack) {
		if (stack.isEmpty() && !(stack.getItem() instanceof BlockItem)) {
			return;
		}
		final Block block = ((BlockItem) stack.getItem()).getBlock();
		placeBlock(pos, block.getDefaultState()); // Use default state currently
		BlockItem.setTileEntityNBT(world, null, pos, stack);
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
	
	private void destroyBlock(PlayerEntity player, BlockPos pos, Inventory inventory) {
		final BlockState state = world.getBlockState(pos);
		final int exp = state.getExpDrop(world, pos, 0, 0);
		
		if (placeBlock(pos, world.getFluidState(pos).getBlockState())) {
			if (world instanceof ServerWorld) {
				Block.getDrops(state, (ServerWorld) world, pos, world.getTileEntity(pos)).stream() //
						.map(inventory::addItem) //
						.filter(Predicates.not(ItemStack::isEmpty)) //
						.forEach(stack -> Block.spawnAsEntity(world, pos, stack));
				state.spawnAdditionalDrops(world, player.getPosition(), ItemStack.EMPTY);
			}
			if (exp > 0) {
				state.getBlock().dropXpOnBlockBreak(world, player.getPosition(), exp);
			}
		}
	}
	
	private List<ItemStack> extractItems(BlockTagItemStackHandler handler, Set<BlockPos> set) {
		return ItemHandlerUtil.extractItems(handler, handler::getCondition, set.size());
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
		return Collections.unmodifiableSet(allPositionSet);
	}
	
	public Set<BlockPos> getFirstRailPos() {
		return firstRailPos;
	}
	
	protected BlockPos addFirstRail(BlockPos pos) {
		firstRailPos.add(pos);
		return pos;
	}
	
	public static Optional<TrackBuilderManager> create(BlockPos rayTracePos, Direction rayTraceFace, World world, Vec3d lookVector, TrackBuilderMode mode, boolean doubleTrack) {
		final TrackBuilderManager manager = doubleTrack ? new DoubleTrackBuilderManager(rayTracePos, rayTraceFace, world, lookVector, mode) : new SingleTrackBuilderManager(rayTracePos, rayTraceFace, world, lookVector, mode);
		if (manager.direction.getAxis().isHorizontal()) {
			manager.calculateBlockPosition();
			return Optional.of(manager);
		}
		return Optional.empty();
	}
	
	private static class SingleTrackBuilderManager extends TrackBuilderManager {
		
		private SingleTrackBuilderManager(BlockPos rayTracePos, Direction rayTraceFace, World world, Vec3d lookVector, TrackBuilderMode mode) {
			super(rayTracePos, rayTraceFace, world, lookVector, mode);
		}
		
		@Override
		protected void calculate(Direction directionLeft, Direction directionRight) {
			// Rails
			BlockPos.getAllInBox(addFirstRail(startPos.offset(direction).up()), startPos.offset(direction, 17).up()) //
					.map(BlockPos::toImmutable) //
					.forEach(railSet::add);
			
			// Redstone torch
			redstoneTorchSet.add(startPos.offset(direction, 9).down().toImmutable());
			
			// Ground blocks
			BlockPos.getAllInBox(startPos.offset(direction).offset(directionLeft), startPos.offset(direction, 17).offset(directionRight)) //
					.map(BlockPos::toImmutable) //
					.forEach(groundBlockSet::add);
			
			// Cobblestone ground
			BlockPos.getAllInBox(startPos.offset(direction).offset(directionLeft).down(), startPos.offset(direction, 17).offset(directionRight).down(2)) //
					.map(BlockPos::toImmutable) //
					.filter(Predicates.not(redstoneTorchSet::contains)) //
					.forEach(cobbleSet::add);
			
			// Air blocks
			if (mode != TrackBuilderMode.MODE_NOAIR) {
				BlockPos.getAllInBox(startPos.offset(direction).offset(directionLeft, mode.getDistanceSide()).up(), startPos.offset(direction, 17).offset(directionRight, mode.getDistanceSide()).up(mode.getDistanceUp())) //
						.map(BlockPos::toImmutable) //
						.filter(Predicates.not(railSet::contains)) //
						.forEach(airSet::add);
			}
		}
	}
	
	private static class DoubleTrackBuilderManager extends TrackBuilderManager {
		
		private DoubleTrackBuilderManager(BlockPos rayTracePos, Direction rayTraceFace, World world, Vec3d lookVector, TrackBuilderMode mode) {
			super(rayTracePos, rayTraceFace, world, lookVector, mode);
		}
		
		@Override
		protected void calculate(Direction directionLeft, Direction directionRight) {
			// Left rails
			BlockPos.getAllInBox(addFirstRail(startPos.offset(direction).offset(directionLeft).up()), startPos.offset(direction, 17).offset(directionLeft).up()) //
					.map(BlockPos::toImmutable) //
					.forEach(railSet::add);
			
			// Left redstone torch
			redstoneTorchSet.add(startPos.offset(direction, 9).offset(directionLeft).down().toImmutable());
			
			// Right rails
			BlockPos.getAllInBox(addFirstRail(startPos.offset(direction).offset(directionRight).up()), startPos.offset(direction, 17).offset(directionRight).up()) //
					.map(BlockPos::toImmutable) //
					.forEach(railSet::add);
			
			// Right redstone torch
			redstoneTorchSet.add(startPos.offset(direction, 9).offset(directionRight).down().toImmutable());
			
			// Ground blocks
			BlockPos.getAllInBox(startPos.offset(direction).offset(directionLeft, 2), startPos.offset(direction, 17).offset(directionRight, 2)) //
					.map(BlockPos::toImmutable) //
					.forEach(groundBlockSet::add);
			
			// Cobblestone ground
			BlockPos.getAllInBox(startPos.offset(direction).offset(directionLeft, 2).down(), startPos.offset(direction, 17).offset(directionRight, 2).down(2)) //
					.map(BlockPos::toImmutable) //
					.filter(Predicates.not(redstoneTorchSet::contains)) //
					.forEach(cobbleSet::add);
			
			// Air blocks
			if (mode != TrackBuilderMode.MODE_NOAIR) {
				BlockPos.getAllInBox(startPos.offset(direction).offset(directionLeft, mode.getDistanceSide() + 1).up(), startPos.offset(direction, 17).offset(directionRight, mode.getDistanceSide() + 1).up(mode.getDistanceUp())) //
						.map(BlockPos::toImmutable) //
						.filter(Predicates.not(railSet::contains)) //
						.forEach(airSet::add);
			}
		}
	}
}
