package info.u_team.useful_railroads.block;

import info.u_team.useful_railroads.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class HighSpeedRailBlock extends CustomPoweredRailBlock {
	
	@Override
	protected void controlSpeed(BlockPos pos, BlockState state, AbstractMinecart cart) {
		final double speedClamp = CommonConfig.getInstance().highspeedRailMaxSpeed.get();
		final double accelOcc = CommonConfig.getInstance().highspeedRailAccelOccupied.get();
		final double accelUnocc = CommonConfig.getInstance().highspeedRailAccelUnoccupied.get();
		
		speedUpCart(cart, cart.isVehicle() ? accelOcc : accelUnocc, speedClamp);
	}
	
	@Override
	public boolean canMakeSlopes(BlockState state, BlockGetter level, BlockPos pos) {
		return false;
	}
	
}
