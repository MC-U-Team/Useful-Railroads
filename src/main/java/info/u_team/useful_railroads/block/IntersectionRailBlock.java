package info.u_team.useful_railroads.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;

public class IntersectionRailBlock extends CustomRailBlock {
	
	@Override
	public RailShape getRailDirection(BlockState state, IBlockReader world, BlockPos pos, AbstractMinecartEntity cart) {
		if (cart != null) {
			final Vector3d motion = cart.getMotion();
			if (Math.abs(motion.getX()) > Math.abs(motion.getZ())) {
				return RailShape.EAST_WEST;
			} else {
				return RailShape.NORTH_SOUTH;
			}
		}
		return super.getRailDirection(state, world, pos, cart);
	}
	
	@Override
	public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isFlexibleRail(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
}
