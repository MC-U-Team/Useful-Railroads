package info.u_team.useful_railroads.block;

import info.u_team.useful_railroads.config.UsefulRailroadsConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class HighSpeedRailBlock extends CustomPoweredRailBlock {
	
	@Override
	protected void controlSpeed(BlockPos pos, BlockState state, AbstractMinecartEntity cart) {
		final double speedClamp = UsefulRailroadsConfig.HIGH_SPEED_RAIL_MAX_SPEED.get();
		final double accelOcc = UsefulRailroadsConfig.HIGH_SPEED_RAIL_ACCEL_OCCUPIED.get();
		final double accelUnocc = UsefulRailroadsConfig.HIGH_SPEED_RAIL_ACCEL_UNOCCUPIED.get();
		
		speedUpCart(cart, cart.isBeingRidden() ? accelOcc : accelUnocc, speedClamp);
	}
	
	@Override
	public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
	
}
