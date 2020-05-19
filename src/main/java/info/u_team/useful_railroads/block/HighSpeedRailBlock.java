package info.u_team.useful_railroads.block;

import info.u_team.useful_railroads.config.Config;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class HighSpeedRailBlock extends CustomPoweredRailBlock {
	
	public HighSpeedRailBlock(String name) {
		super(name);
	}
	
	@Override
	protected void controllSpeed(BlockPos pos, BlockState state, AbstractMinecartEntity cart) {
		final double speedClamp = Config.HIGH_SPEED_RAIL_MAX_SPEED.get();
		final double accelOcc = Config.HIGH_SPEED_RAIL_ACCEL_OCCUPIED.get();
		final double accelUnocc = Config.HIGH_SPEED_RAIL_ACCEL_UNOCCUPIED.get();

		speedUpCart(cart, cart.isBeingRidden() ? accelOcc : accelUnocc, speedClamp);
	}
	
	@Override
	public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
	
}
