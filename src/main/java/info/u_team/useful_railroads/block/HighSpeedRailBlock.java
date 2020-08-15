package info.u_team.useful_railroads.block;

import info.u_team.useful_railroads.config.CommonConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class HighSpeedRailBlock extends CustomPoweredRailBlock {
	
	@Override
	protected void controlSpeed(BlockPos pos, BlockState state, AbstractMinecartEntity cart) {
		final double speedClamp = CommonConfig.getInstance().highspeedRailMaxSpeed.get();
		final double accelOcc = CommonConfig.getInstance().highspeedRailAccelOccupied.get();
		final double accelUnocc = CommonConfig.getInstance().highspeedRailAccelUnoccupied.get();
		
		speedUpCart(cart, cart.isBeingRidden() ? accelOcc : accelUnocc, speedClamp);
	}
	
	@Override
	public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
	
}
