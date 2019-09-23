package info.u_team.useful_railroads.util;

import java.util.*;

import net.minecraft.util.Direction;
import net.minecraft.util.Direction.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class TrackBuilderManager {
	
	private boolean errorOccured;
	
	private final Set<BlockPos> railSet = new HashSet<>();
	private final Set<BlockPos> groundBlockSet = new HashSet<>();
	private final Set<BlockPos> redstoneTorchSet = new HashSet<>();
	private final Set<BlockPos> cobbleSet = new HashSet<>();
	private final Set<BlockPos> airSet = new HashSet<>();
	
	public TrackBuilderManager(BlockPos rayTracePos, Direction rayTraceFace, World world, Vec3d lookVector) {
		final Direction direction = Direction.getFacingFromVector(lookVector.x, lookVector.y, lookVector.z);
		
		if (rayTraceFace.getAxis().isHorizontal()) {
			rayTracePos = rayTracePos.offset(direction.getOpposite());
		}
		if (!world.getBlockState(rayTracePos).isSolid()) {
			rayTracePos = rayTracePos.down();
		}
		
		final BlockPos pos = rayTracePos.toImmutable();
		
		if (direction.getAxis().isVertical()) {
			errorOccured = true;
			return;
		}
		final Axis crossWiseAxis = direction.rotateY().getAxis();
		
		final Direction crossWisePositiveDirection = Direction.getFacingFromAxisDirection(crossWiseAxis, AxisDirection.POSITIVE);
		final Direction crossWiseNegativeDirection = Direction.getFacingFromAxisDirection(crossWiseAxis, AxisDirection.NEGATIVE);
		
		BlockPos.getAllInBox(pos.offset(direction).up(), pos.offset(direction, 16).up()) //
				.map(BlockPos::toImmutable) //
				.forEach(railSet::add);
		
		BlockPos.getAllInBox(pos.offset(direction).offset(crossWisePositiveDirection), pos.offset(direction, 16).offset(crossWiseNegativeDirection)) //
				.map(BlockPos::toImmutable) //
				.forEach(groundBlockSet::add);
		
		redstoneTorchSet.add(pos.offset(direction, 8).down().toImmutable());
		
		BlockPos.getAllInBox(pos.offset(direction).offset(crossWisePositiveDirection).down(), pos.offset(direction, 16).offset(crossWiseNegativeDirection).down(2)) //
				.map(BlockPos::toImmutable) //
				.filter(Predicates.not(redstoneTorchSet::contains)) //
				.forEach(cobbleSet::add);
		
		BlockPos.getAllInBox(pos.offset(direction).offset(crossWisePositiveDirection).up(1), pos.offset(direction, 16).offset(crossWiseNegativeDirection).up(3)) //
				.map(BlockPos::toImmutable) //
				.filter(Predicates.not(railSet::contains)) //
				.forEach(airSet::add);
	}
	
	public boolean hasErrorOccured() {
		return errorOccured;
	}
	
	public Set<BlockPos> getRenderSet() {
		final Set<BlockPos> set = new HashSet<>();
		set.addAll(railSet);
		set.addAll(groundBlockSet);
		set.addAll(redstoneTorchSet);
		set.addAll(cobbleSet);
		set.addAll(airSet);
		return set;
	}
}
