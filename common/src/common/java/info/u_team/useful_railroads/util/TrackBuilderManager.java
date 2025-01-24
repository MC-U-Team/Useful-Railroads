package info.u_team.useful_railroads.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import com.google.common.base.Predicates;

import info.u_team.useful_railroads.inventory.BlockTagItemStackHandler;
import info.u_team.useful_railroads.inventory.TrackBuilderInventoryWrapper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class TrackBuilderManager {
	
	protected final Level level;
	protected final Direction direction;
	protected final BlockPos startPos;
	
	protected final TrackBuilderMode mode;
	
	protected final Set<BlockPos> allPositionSet = new HashSet<>();
	
	protected final Set<BlockPos> firstRailPos = new HashSet<>();
	protected final Set<BlockPos> railSet = new HashSet<>();
	protected final Set<BlockPos> groundSet = new HashSet<>();
	protected final Set<BlockPos> redstoneTorchSet = new HashSet<>();
	protected final Set<BlockPos> cobbleSet = new HashSet<>();
	protected final Set<BlockPos> airSet = new HashSet<>();
	protected final Set<BlockPos> tunnelSet = new HashSet<>();
	protected final Set<BlockPos> torchSet = new HashSet<>();
	
	private TrackBuilderManager(BlockPos rayTracePos, Direction rayTraceFace, Level level, Vec3 lookVector, TrackBuilderMode mode) {
		this.level = level;
		direction = Direction.getNearest(lookVector.x, lookVector.y, lookVector.z);
		
		if (rayTraceFace.getAxis().isHorizontal()) {
			rayTracePos = rayTracePos.relative(direction.getOpposite());
		}
		if (!level.getBlockState(rayTracePos).canOcclude()) {
			rayTracePos = rayTracePos.below();
		}
		startPos = rayTracePos.immutable();
		this.mode = mode;
	}
	
	public void calculateBlockPosition() {
		final Direction directionLeft = direction.getCounterClockWise();
		final Direction directionRight = direction.getClockWise();
		calculate(directionLeft, directionRight);
		Stream.of(railSet, groundSet, tunnelSet, redstoneTorchSet, torchSet, cobbleSet, airSet).flatMap(Set::stream).forEach(allPositionSet::add);
	}
	
	protected abstract void calculate(Direction directionLeft, Direction directionRight);
	
	public void execute(Player player, TrackBuilderInventoryWrapper wrapper) {
		if (level.isClientSide || !(level instanceof ServerLevel)) {
			return;
		}
		
		final int cost = calculateCost();
		if (wrapper.getFuel() < cost) {
			player.sendSystemMessage(Component.translatable("item.usefulrailroads.track_builder.not_enough_fuel", cost).withStyle(ChatFormatting.RED));
			return;
		}
		
		if (!hasEnoughItems(wrapper.getRailInventory(), railSet) || !hasEnoughItems(wrapper.getGroundInventory(), groundSet) || !hasEnoughItems(wrapper.getTunnelInventory(), tunnelSet) || !hasEnoughItems(wrapper.getRedstoneTorchInventory(), redstoneTorchSet) || !hasEnoughItems(wrapper.getTorchInventory(), torchSet)) {
			player.sendSystemMessage(Component.translatable("item.usefulrailroads.track_builder.not_enough_blocks").withStyle(ChatFormatting.RED));
			return;
		}
		
		wrapper.setFuel(wrapper.getFuel() - cost);
		
		final List<ItemStack> railStacks = extractItems(wrapper.getRailInventory(), railSet);
		final List<ItemStack> groundStacks = extractItems(wrapper.getGroundInventory(), groundSet);
		final List<ItemStack> tunnelStacks = extractItems(wrapper.getTunnelInventory(), tunnelSet);
		final List<ItemStack> redstoneTorchStacks = extractItems(wrapper.getRedstoneTorchInventory(), redstoneTorchSet);
		final List<ItemStack> torchStacks = extractItems(wrapper.getTorchInventory(), torchSet);
		
		final SimpleContainer dropInventory = new SimpleContainer(50);
		allPositionSet.stream().filter(Predicates.not(level::isEmptyBlock)).forEach(pos -> destroyBlock(player, pos, dropInventory));
		Containers.dropContents(level, player, dropInventory);
		
		cobbleSet.forEach(pos -> placeBlock(pos, Blocks.COBBLESTONE.defaultBlockState()));
		redstoneTorchSet.forEach(pos -> placeItemBlock(pos, ItemHandlerUtil.getOneItemAndRemove(redstoneTorchStacks)));
		groundSet.forEach(pos -> placeItemBlock(pos, ItemHandlerUtil.getOneItemAndRemove(groundStacks)));
		railSet.forEach(pos -> placeItemBlock(pos, ItemHandlerUtil.getOneItemAndRemove(railStacks)));
		tunnelSet.forEach(pos -> placeItemBlock(pos, ItemHandlerUtil.getOneItemAndRemove(tunnelStacks)));
		torchSet.forEach(pos -> placeItemBlock(pos, ItemHandlerUtil.getOneItemAndRemove(torchStacks), (item, block) -> {
			final boolean redstoneTorch = item == Items.REDSTONE_TORCH;
			if (redstoneTorch || item == Items.TORCH) {
				final Block torchWall = redstoneTorch ? Blocks.REDSTONE_WALL_TORCH : Blocks.WALL_TORCH;
				final Block torchGround = redstoneTorch ? Blocks.REDSTONE_TORCH : Blocks.TORCH;
				
				return Stream.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST) //
						.map(direction -> torchWall.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, direction)) //
						.filter(state -> state.canSurvive(level, pos)) //
						.findAny() //
						.orElseGet(torchGround::defaultBlockState);
			}
			return block.defaultBlockState();
		}));
		
		wrapper.writeItemStack();
	}
	
	private void placeItemBlock(BlockPos pos, ItemStack stack) {
		placeItemBlock(pos, stack, (item, block) -> block.defaultBlockState()); // Use default state normally
	}
	
	private void placeItemBlock(BlockPos pos, ItemStack stack, BiFunction<Item, Block, BlockState> function) {
		if (stack.isEmpty() && !(stack.getItem() instanceof BlockItem)) {
			return;
		}
		final Block block = ((BlockItem) stack.getItem()).getBlock();
		placeBlock(pos, function.apply(stack.getItem(), block));
		BlockItem.updateCustomBlockEntityTag(level, null, pos, stack);
	}
	
	private boolean placeBlock(BlockPos pos, BlockState state) {
		return placeBlock(pos, state, 3);
	}
	
	private boolean placeBlock(BlockPos pos, BlockState state, int flag) {
		final boolean blockSnapshotValue = level.captureBlockSnapshots;
		level.captureBlockSnapshots = false; // Disable capture block snapshots here because else the client will not receive updates
		final boolean placed = level.setBlock(pos, state, flag);
		level.captureBlockSnapshots = blockSnapshotValue;
		return placed;
	}
	
	private void destroyBlock(Player player, BlockPos pos, SimpleContainer inventory) {
		final BlockState state = level.getBlockState(pos);
		final int exp = state.getExpDrop(level, player.getRandom(), pos, 0, 0);
		
		if (placeBlock(pos, Blocks.AIR.defaultBlockState())) { // Normally we place the fluid state but we don't want fluids here
			if (level instanceof ServerLevel) {
				Block.getDrops(state, (ServerLevel) level, pos, level.getBlockEntity(pos)).stream() //
						.map(inventory::addItem) //
						.filter(Predicates.not(ItemStack::isEmpty)) //
						.forEach(stack -> Block.popResource(level, pos, stack));
				state.spawnAfterBreak((ServerLevel) level, player.blockPosition(), ItemStack.EMPTY, true /* TODO TRUE?? */);
				if (exp > 0) {
					state.getBlock().popExperience((ServerLevel) level, player.blockPosition(), exp);
				}
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
		final int breakCount = (int) allPositionSet.stream().filter(Predicates.not(level::isEmptyBlock)).count();
		final int placeCount = allPositionSet.size() - airSet.size();
		
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
	
	public static Optional<TrackBuilderManager> create(BlockPos rayTracePos, Direction rayTraceFace, Level level, Vec3 lookVector, TrackBuilderMode mode, boolean doubleTrack) {
		final TrackBuilderManager manager = doubleTrack ? new DoubleTrackBuilderManager(rayTracePos, rayTraceFace, level, lookVector, mode) : new SingleTrackBuilderManager(rayTracePos, rayTraceFace, level, lookVector, mode);
		if (manager.direction.getAxis().isHorizontal()) {
			manager.calculateBlockPosition();
			return Optional.of(manager);
		}
		return Optional.empty();
	}
	
	private static class SingleTrackBuilderManager extends TrackBuilderManager {
		
		private SingleTrackBuilderManager(BlockPos rayTracePos, Direction rayTraceFace, Level level, Vec3 lookVector, TrackBuilderMode mode) {
			super(rayTracePos, rayTraceFace, level, lookVector, mode);
		}
		
		@Override
		protected void calculate(Direction directionLeft, Direction directionRight) {
			// Rails
			BlockPos.betweenClosedStream(addFirstRail(startPos.relative(direction).above()), startPos.relative(direction, 17).above()) //
					.map(BlockPos::immutable) //
					.forEach(railSet::add);
			
			// Redstone torch
			redstoneTorchSet.add(startPos.relative(direction, 9).below().immutable());
			
			// Ground blocks
			BlockPos.betweenClosedStream(startPos.relative(direction).relative(directionLeft), startPos.relative(direction, 17).relative(directionRight)) //
					.map(BlockPos::immutable) //
					.forEach(groundSet::add);
			
			// Cobblestone ground
			BlockPos.betweenClosedStream(startPos.relative(direction).relative(directionLeft).below(), startPos.relative(direction, 17).relative(directionRight).below(2)) //
					.map(BlockPos::immutable) //
					.filter(Predicates.not(redstoneTorchSet::contains)) //
					.forEach(cobbleSet::add);
			
			if (mode.isFullTunnel()) {
				// Air blocks (without removed blocks still)
				BlockPos.betweenClosedStream(startPos.relative(direction).relative(directionLeft, 1).above(), startPos.relative(direction, 17).relative(directionRight, 1).above(4)) //
						.map(BlockPos::immutable) //
						.forEach(airSet::add);
				
				// Tunnel blocks
				BlockPos.betweenClosedStream(startPos.relative(direction).relative(directionLeft, 2).above(), startPos.relative(direction, 17).relative(directionRight, 2).above(5)) //
						.map(BlockPos::immutable) //
						.filter(Predicates.not(airSet::contains)) //
						.forEach(tunnelSet::add);
				
				// Torch blocks
				torchSet.add(startPos.relative(direction, 9 - 4).above(3).relative(directionLeft, 1).immutable());
				torchSet.add(startPos.relative(direction, 9 + 4).above(3).relative(directionLeft, 1).immutable());
				torchSet.add(startPos.relative(direction, 9 - 4).above(3).relative(directionRight, 1).immutable());
				torchSet.add(startPos.relative(direction, 9 + 4).above(3).relative(directionRight, 1).immutable());
				
				// Remove replaced blocks from air blocks
				airSet.removeAll(torchSet);
				airSet.removeAll(railSet);
				
			} else if (!mode.isNoTunnel()) {
				// Air blocks
				BlockPos.betweenClosedStream(startPos.relative(direction).relative(directionLeft, mode.getDistanceSide()).above(), startPos.relative(direction, 17).relative(directionRight, mode.getDistanceSide()).above(mode.getDistanceUp())) //
						.map(BlockPos::immutable) //
						.filter(Predicates.not(railSet::contains)) //
						.forEach(airSet::add);
			}
		}
	}
	
	private static class DoubleTrackBuilderManager extends TrackBuilderManager {
		
		private DoubleTrackBuilderManager(BlockPos rayTracePos, Direction rayTraceFace, Level level, Vec3 lookVector, TrackBuilderMode mode) {
			super(rayTracePos, rayTraceFace, level, lookVector, mode);
		}
		
		@Override
		protected void calculate(Direction directionLeft, Direction directionRight) {
			// Left rails
			BlockPos.betweenClosedStream(addFirstRail(startPos.relative(direction).relative(directionLeft).above()), startPos.relative(direction, 17).relative(directionLeft).above()) //
					.map(BlockPos::immutable) //
					.forEach(railSet::add);
			
			// Left redstone torch
			redstoneTorchSet.add(startPos.relative(direction, 9).relative(directionLeft).below().immutable());
			
			// Right rails
			BlockPos.betweenClosedStream(addFirstRail(startPos.relative(direction).relative(directionRight).above()), startPos.relative(direction, 17).relative(directionRight).above()) //
					.map(BlockPos::immutable) //
					.forEach(railSet::add);
			
			// Right redstone torch
			redstoneTorchSet.add(startPos.relative(direction, 9).relative(directionRight).below().immutable());
			
			// Ground blocks
			BlockPos.betweenClosedStream(startPos.relative(direction).relative(directionLeft, 2), startPos.relative(direction, 17).relative(directionRight, 2)) //
					.map(BlockPos::immutable) //
					.forEach(groundSet::add);
			
			// Cobblestone ground
			BlockPos.betweenClosedStream(startPos.relative(direction).relative(directionLeft, 2).below(), startPos.relative(direction, 17).relative(directionRight, 2).below(2)) //
					.map(BlockPos::immutable) //
					.filter(Predicates.not(redstoneTorchSet::contains)) //
					.forEach(cobbleSet::add);
			
			if (mode.isFullTunnel()) {
				// Air blocks (without removed blocks still)
				BlockPos.betweenClosedStream(startPos.relative(direction).relative(directionLeft, 2).above(), startPos.relative(direction, 17).relative(directionRight, 2).above(4)) //
						.map(BlockPos::immutable) //
						.forEach(airSet::add);
				
				// Tunnel blocks
				BlockPos.betweenClosedStream(startPos.relative(direction).relative(directionLeft, 3).above(), startPos.relative(direction, 17).relative(directionRight, 3).above(5)) //
						.map(BlockPos::immutable) //
						.filter(Predicates.not(airSet::contains)) //
						.forEach(tunnelSet::add);
				
				// Torch blocks
				torchSet.add(startPos.relative(direction, 3).above(3).relative(directionLeft, 2).immutable());
				torchSet.add(startPos.relative(direction, 7).above(3).relative(directionLeft, 2).immutable());
				torchSet.add(startPos.relative(direction, 11).above(3).relative(directionLeft, 2).immutable());
				torchSet.add(startPos.relative(direction, 15).above(3).relative(directionLeft, 2).immutable());
				
				torchSet.add(startPos.relative(direction, 3).above(3).relative(directionRight, 2).immutable());
				torchSet.add(startPos.relative(direction, 7).above(3).relative(directionRight, 2).immutable());
				torchSet.add(startPos.relative(direction, 11).above(3).relative(directionRight, 2).immutable());
				torchSet.add(startPos.relative(direction, 15).above(3).relative(directionRight, 2).immutable());
				
				// Remove replaced blocks from air blocks
				airSet.removeAll(torchSet);
				airSet.removeAll(railSet);
				
			} else if (!mode.isNoTunnel()) {
				// Air blocks
				BlockPos.betweenClosedStream(startPos.relative(direction).relative(directionLeft, mode.getDistanceSide() + 1).above(), startPos.relative(direction, 17).relative(directionRight, mode.getDistanceSide() + 1).above(mode.getDistanceUp())) //
						.map(BlockPos::immutable) //
						.filter(Predicates.not(railSet::contains)) //
						.forEach(airSet::add);
			}
		}
	}
}
