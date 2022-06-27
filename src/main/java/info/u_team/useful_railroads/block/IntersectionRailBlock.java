package info.u_team.useful_railroads.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

public class IntersectionRailBlock extends CustomRailBlock {
	
	@Override
	public RailShape getRailDirection(BlockState state, BlockGetter level, BlockPos pos, AbstractMinecart cart) {
		if (cart != null) {
			final Vec3 motion = cart.getDeltaMovement();
			if (Math.abs(motion.x()) > Math.abs(motion.z())) {
				return RailShape.EAST_WEST;
			} else {
				return RailShape.NORTH_SOUTH;
			}
		}
		return super.getRailDirection(state, level, pos, cart);
	}
	
	@Override
	public boolean canMakeSlopes(BlockState state, BlockGetter level, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isFlexibleRail(BlockState state, BlockGetter level, BlockPos pos) {
		return false;
	}
}
