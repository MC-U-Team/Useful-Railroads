package info.u_team.useful_railroads.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockReader;

public class SpeedClampRailBlock extends CustomPoweredRailBlock {

	public SpeedClampRailBlock(String name) {
		super(name);
	}

	@Override
	protected void controllSpeed(BlockPos pos, BlockState state, AbstractMinecartEntity cart) {
		final double clampVelocity = 0.25D; // hard-coded for now
		final double currentVelocity = cart.getMotion().length();

		Vec3d direction = Vec3d.ZERO;
		if (currentVelocity < 0.1D) {
			if (cart.getPassengers().size() > 0) {
				direction = cart.getPassengers().get(0).getMotion().normalize();
			}
		} else {
			direction = cart.getMotion().normalize();
		}

		cart.setMotion(new Vec3d(
			direction.getX() * clampVelocity,
			direction.getY() * clampVelocity,
			direction.getZ() * clampVelocity
		));
	}

	@Override
	public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}

}
