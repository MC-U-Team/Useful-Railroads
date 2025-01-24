package info.u_team.useful_railroads.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.u_team_core.util.MathUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeUtil {
	
	public static List<VoxelShape> createVoxelShapeFromVector(List<Pair<Vec3, Vec3>> list) {
		return list.stream().map(pair -> {
			final Vec3 vec1 = pair.getLeft();
			final Vec3 vec2 = pair.getRight();
			final AABB aabb = new AABB(vec1, vec2);
			
			return Block.box(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
		}).collect(Collectors.toList());
	}
	
	public static Pair<Vec3, Vec3> createVectorPair(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Pair.of(new Vec3(x1, y1, z1), new Vec3(x2, y2, z2));
	}
	
	public static List<Pair<Vec3, Vec3>> rotateAroundY(List<Pair<Vec3, Vec3>> list, double angle) {
		return list.stream().map(pair -> {
			final Vec3 vec1 = MathUtil.rotateVectorAroundYCC(pair.getLeft().subtract(8, 8, 8), angle).add(8, 8, 8);
			final Vec3 vec2 = MathUtil.rotateVectorAroundYCC(pair.getRight().subtract(8, 8, 8), angle).add(8, 8, 8);
			return Pair.of(vec1, vec2);
		}).collect(Collectors.toList());
	}
	
	public static Map<Direction, List<Pair<Vec3, Vec3>>> getHorizontalRotations(List<Pair<Vec3, Vec3>> northShape) {
		final Map<Direction, List<Pair<Vec3, Vec3>>> shapes = new HashMap<>();
		shapes.put(Direction.NORTH, northShape);
		shapes.put(Direction.EAST, rotateAroundY(northShape, -Math.PI / 2));
		shapes.put(Direction.SOUTH, rotateAroundY(northShape, Math.PI));
		shapes.put(Direction.WEST, rotateAroundY(northShape, Math.PI / 2));
		return shapes;
	}
	
}
