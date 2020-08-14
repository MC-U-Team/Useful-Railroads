package info.u_team.useful_railroads.util;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.u_team_core.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;

public class VoxelShapeUtil {
	
	public static List<VoxelShape> createVoxelShapeFromVector(List<Pair<Vec3d, Vec3d>> list) {
		return list.stream().map(pair -> {
			final Vec3d vec1 = pair.getLeft();
			final Vec3d vec2 = pair.getRight();
			return Block.makeCuboidShape(vec1.getX(), vec1.getY(), vec1.getZ(), vec2.getX(), vec2.getY(), vec2.getZ());
		}).collect(Collectors.toList());
	}
	
	public static Pair<Vec3d, Vec3d> createVectorPair(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Pair.of(new Vec3d(x1, y1, z1), new Vec3d(x2, y2, z2));
	}
	
	public static List<Pair<Vec3d, Vec3d>> rotateAroundY(List<Pair<Vec3d, Vec3d>> list, double angle) {
		return list.stream().map(pair -> {
			final Vec3d vec1 = MathUtil.rotateVectorAroundYCC(pair.getLeft().subtract(8, 8, 8), angle).add(8, 8, 8);
			final Vec3d vec2 = MathUtil.rotateVectorAroundYCC(pair.getRight().subtract(8, 8, 8), angle).add(8, 8, 8);
			return Pair.of(vec1, vec2);
		}).collect(Collectors.toList());
	}
	
	public static Map<Direction, List<Pair<Vec3d, Vec3d>>> getHorizontalRotations(List<Pair<Vec3d, Vec3d>> northShape) {
		final Map<Direction, List<Pair<Vec3d, Vec3d>>> shapes = new HashMap<>();
		shapes.put(Direction.NORTH, northShape);
		shapes.put(Direction.EAST, rotateAroundY(northShape, -Math.PI / 2));
		shapes.put(Direction.SOUTH, rotateAroundY(northShape, Math.PI));
		shapes.put(Direction.WEST, rotateAroundY(northShape, Math.PI / 2));
		return shapes;
	}
	
}
