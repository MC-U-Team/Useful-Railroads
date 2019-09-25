package info.u_team.useful_railroads.util;

import java.util.*;
import java.util.stream.Stream;

import info.u_team.useful_railroads.inventory.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TrackBuilderManager {
	
	private final World world;
	private final Direction direction;
	private final BlockPos startPos;
	
	private final BlockPos firstRailPos;
	
	private final TrackBuilderMode mode;
	
	private final Set<BlockPos> allPositionSet = new HashSet<>();
	
	private final Set<BlockPos> railSet = new HashSet<>();
	private final Set<BlockPos> groundBlockSet = new HashSet<>();
	private final Set<BlockPos> redstoneTorchSet = new HashSet<>();
	private final Set<BlockPos> cobbleSet = new HashSet<>();
	private final Set<BlockPos> airSet = new HashSet<>();
	
	public TrackBuilderManager(BlockPos rayTracePos, Direction rayTraceFace, World world, Vec3d lookVector, TrackBuilderMode mode) {
		this.world = world;
		direction = Direction.getFacingFromVector(lookVector.x, lookVector.y, lookVector.z);
		
		if (rayTraceFace.getAxis().isHorizontal()) {
			rayTracePos = rayTracePos.offset(direction.getOpposite());
		}
		if (!world.getBlockState(rayTracePos).isSolid()) {
			rayTracePos = rayTracePos.down();
		}
		startPos = rayTracePos.toImmutable();
		firstRailPos = startPos.offset(direction).up();
		
		this.mode = mode;
	}
	
	public boolean calculateBlockPosition() {
		if (direction.getAxis().isVertical()) {
			return false;
		}
		
		final Axis crossWiseAxis = direction.rotateY().getAxis();
		
		final Direction crossWisePositiveDirection = Direction.getFacingFromAxisDirection(crossWiseAxis, AxisDirection.POSITIVE);
		final Direction crossWiseNegativeDirection = Direction.getFacingFromAxisDirection(crossWiseAxis, AxisDirection.NEGATIVE);
		
		BlockPos.getAllInBox(startPos.offset(direction).up(), startPos.offset(direction, 17).up()) //
				.map(BlockPos::toImmutable) //
				.forEach(railSet::add);
		
		BlockPos.getAllInBox(startPos.offset(direction).offset(crossWisePositiveDirection), startPos.offset(direction, 17).offset(crossWiseNegativeDirection)) //
				.map(BlockPos::toImmutable) //
				.forEach(groundBlockSet::add);
		
		redstoneTorchSet.add(startPos.offset(direction, 9).down().toImmutable());
		
		BlockPos.getAllInBox(startPos.offset(direction).offset(crossWisePositiveDirection).down(), startPos.offset(direction, 17).offset(crossWiseNegativeDirection).down(2)) //
				.map(BlockPos::toImmutable) //
				.filter(Predicates.not(redstoneTorchSet::contains)) //
				.forEach(cobbleSet::add);
		
		if (mode != TrackBuilderMode.MODE_NOAIR) {
			BlockPos.getAllInBox(startPos.offset(direction).offset(crossWisePositiveDirection, mode.getDistanceSide()).up(1), startPos.offset(direction, 17).offset(crossWiseNegativeDirection, mode.getDistanceSide()).up(mode.getDistanceUp())) //
					.map(BlockPos::toImmutable) //
					.filter(Predicates.not(railSet::contains)) //
					.forEach(airSet::add);
		}
		
		Stream.of(railSet, groundBlockSet, redstoneTorchSet, cobbleSet, airSet).flatMap(Set::stream).forEach(allPositionSet::add);
		
		return true;
	}
	
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
	
	public BlockPos getFirstRailPos() {
		return firstRailPos;
	}
}
