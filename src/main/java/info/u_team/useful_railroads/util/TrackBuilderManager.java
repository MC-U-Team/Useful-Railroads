package info.u_team.useful_railroads.util;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

import info.u_team.useful_railroads.inventory.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
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
	protected final Set<BlockPos> groundSet = new HashSet<>();
	protected final Set<BlockPos> redstoneTorchSet = new HashSet<>();
	protected final Set<BlockPos> cobbleSet = new HashSet<>();
	protected final Set<BlockPos> airSet = new HashSet<>();
	protected final Set<BlockPos> tunnelSet = new HashSet<>();
	protected final Set<BlockPos> torchSet = new HashSet<>();
	
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
		Stream.of(railSet, groundSet, tunnelSet, redstoneTorchSet, torchSet, cobbleSet, airSet).flatMap(Set::stream).forEach(allPositionSet::add);
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
		
		if (!hasEnoughItems(wrapper.getRailInventory(), railSet) || !hasEnoughItems(wrapper.getGroundInventory(), groundSet) || !hasEnoughItems(wrapper.getTunnelInventory(), tunnelSet) || !hasEnoughItems(wrapper.getRedstoneTorchInventory(), redstoneTorchSet) || !hasEnoughItems(wrapper.getTorchInventory(), torchSet)) {
			player.sendMessage(new TranslationTextComponent("item.usefulrailroads.track_builder.not_enough_blocks").setStyle(new Style().setColor(TextFormatting.RED)));
			return;
		}
		
		wrapper.setFuel(wrapper.getFuel() - cost);
		
		final List<ItemStack> railStacks = extractItems(wrapper.getRailInventory(), railSet);
		final List<ItemStack> groundStacks = extractItems(wrapper.getGroundInventory(), groundSet);
		final List<ItemStack> tunnelStacks = extractItems(wrapper.getTunnelInventory(), tunnelSet);
		final List<ItemStack> redstoneTorchStacks = extractItems(wrapper.getRedstoneTorchInventory(), redstoneTorchSet);
		final List<ItemStack> torchStacks = extractItems(wrapper.getTorchInventory(), torchSet);
		
		final Inventory dropInventory = new Inventory(50);
		allPositionSet.stream().filter(Predicates.not(world::isAirBlock)).forEach(pos -> destroyBlock(player, pos, dropInventory));
		InventoryHelper.dropInventoryItems(world, player, dropInventory);
		
		cobbleSet.forEach(pos -> placeBlock(pos, Blocks.COBBLESTONE.getDefaultState()));
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
						.map(direction -> torchWall.getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, direction)) //
						.filter(state -> state.isValidPosition(world, pos)) //
						.findAny() //
						.orElseGet(torchGround::getDefaultState);
			}
			return block.getDefaultState();
		}));
		
		wrapper.writeItemStack();
	}
	
	private void placeItemBlock(BlockPos pos, ItemStack stack) {
		placeItemBlock(pos, stack, (item, block) -> block.getDefaultState()); // Use default state normally
	}
	
	private void placeItemBlock(BlockPos pos, ItemStack stack, BiFunction<Item, Block, BlockState> function) {
		if (stack.isEmpty() && !(stack.getItem() instanceof BlockItem)) {
			return;
		}
		final Block block = ((BlockItem) stack.getItem()).getBlock();
		placeBlock(pos, function.apply(stack.getItem(), block));
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
		
		if (placeBlock(pos, Blocks.AIR.getDefaultState())) { // Normally we place the fluid state but we don't want fluids here
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
					.forEach(groundSet::add);
			
			// Cobblestone ground
			BlockPos.getAllInBox(startPos.offset(direction).offset(directionLeft).down(), startPos.offset(direction, 17).offset(directionRight).down(2)) //
					.map(BlockPos::toImmutable) //
					.filter(Predicates.not(redstoneTorchSet::contains)) //
					.forEach(cobbleSet::add);
			
			if (mode.isFullTunnel()) {
				// Air blocks (without removed blocks still)
				BlockPos.getAllInBox(startPos.offset(direction).offset(directionLeft, 1).up(), startPos.offset(direction, 17).offset(directionRight, 1).up(4)) //
						.map(BlockPos::toImmutable) //
						.forEach(airSet::add);
				
				// Tunnel blocks
				BlockPos.getAllInBox(startPos.offset(direction).offset(directionLeft, 2).up(), startPos.offset(direction, 17).offset(directionRight, 2).up(5)) //
						.map(BlockPos::toImmutable) //
						.filter(Predicates.not(airSet::contains)) //
						.forEach(tunnelSet::add);
				
				// Torch blocks
				torchSet.add(startPos.offset(direction, 9).up(3).offset(directionLeft, 1).toImmutable());
				torchSet.add(startPos.offset(direction, 9).up(3).offset(directionRight, 1).toImmutable());
				
				// Remove replaced blocks from air blocks
				airSet.removeAll(torchSet);
				airSet.removeAll(railSet);
				
			} else if (!mode.isNoTunnel()) {
				// Air blocks
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
					.forEach(groundSet::add);
			
			// Cobblestone ground
			BlockPos.getAllInBox(startPos.offset(direction).offset(directionLeft, 2).down(), startPos.offset(direction, 17).offset(directionRight, 2).down(2)) //
					.map(BlockPos::toImmutable) //
					.filter(Predicates.not(redstoneTorchSet::contains)) //
					.forEach(cobbleSet::add);
			
			if (mode.isFullTunnel()) {
				// Air blocks (without removed blocks still)
				BlockPos.getAllInBox(startPos.offset(direction).offset(directionLeft, 2).up(), startPos.offset(direction, 17).offset(directionRight, 2).up(4)) //
						.map(BlockPos::toImmutable) //
						.forEach(airSet::add);
				
				// Tunnel blocks
				BlockPos.getAllInBox(startPos.offset(direction).offset(directionLeft, 3).up(), startPos.offset(direction, 17).offset(directionRight, 3).up(5)) //
						.map(BlockPos::toImmutable) //
						.filter(Predicates.not(airSet::contains)) //
						.forEach(tunnelSet::add);
				
				// Torch blocks
				torchSet.add(startPos.offset(direction, 9).up(3).offset(directionLeft, 2).toImmutable());
				torchSet.add(startPos.offset(direction, 9).up(3).offset(directionRight, 2).toImmutable());
				
				// Remove replaced blocks from air blocks
				airSet.removeAll(torchSet);
				airSet.removeAll(railSet);
				
			} else if (!mode.isNoTunnel()) {
				// Air blocks
				BlockPos.getAllInBox(startPos.offset(direction).offset(directionLeft, mode.getDistanceSide() + 1).up(), startPos.offset(direction, 17).offset(directionRight, mode.getDistanceSide() + 1).up(mode.getDistanceUp())) //
						.map(BlockPos::toImmutable) //
						.filter(Predicates.not(railSet::contains)) //
						.forEach(airSet::add);
			}
		}
	}
}
