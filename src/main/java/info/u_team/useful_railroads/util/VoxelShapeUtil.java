package info.u_team.useful_railroads.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.u_team_core.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;

public class VoxelShapeUtil {
	
	public static List<VoxelShape> createVoxelShapeFromVector(List<Pair<Vector3d, Vector3d>> list) {
		return list.stream().map(pair -> {
			final Vector3d vec1 = pair.getLeft();
			final Vector3d vec2 = pair.getRight();
			return Block.makeCuboidShape(vec1.getX(), vec1.getY(), vec1.getZ(), vec2.getX(), vec2.getY(), vec2.getZ());
		}).collect(Collectors.toList());
	}
	
	public static Pair<Vector3d, Vector3d> createVectorPair(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Pair.of(new Vector3d(x1, y1, z1), new Vector3d(x2, y2, z2));
	}
	
	public static List<Pair<Vector3d, Vector3d>> rotateAroundY(List<Pair<Vector3d, Vector3d>> list, double angle) {
		return list.stream().map(pair -> {
			final Vector3d vec1 = MathUtil.rotateVectorAroundYCC(pair.getLeft().subtract(8, 8, 8), angle).add(8, 8, 8);
			final Vector3d vec2 = MathUtil.rotateVectorAroundYCC(pair.getRight().subtract(8, 8, 8), angle).add(8, 8, 8);
			return Pair.of(vec1, vec2);
		}).collect(Collectors.toList());
	}
	
	public static Map<Direction, List<Pair<Vector3d, Vector3d>>> getHorizontalRotations(List<Pair<Vector3d, Vector3d>> northShape) {
		final Map<Direction, List<Pair<Vector3d, Vector3d>>> shapes = new HashMap<>();
		shapes.put(Direction.NORTH, northShape);
		shapes.put(Direction.EAST, rotateAroundY(northShape, -Math.PI / 2));
		shapes.put(Direction.SOUTH, rotateAroundY(northShape, Math.PI));
		shapes.put(Direction.WEST, rotateAroundY(northShape, Math.PI / 2));
		return shapes;
	}
	
}
