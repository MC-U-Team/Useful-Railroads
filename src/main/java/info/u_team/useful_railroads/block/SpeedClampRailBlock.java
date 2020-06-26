package info.u_team.useful_railroads.block;

import info.u_team.useful_railroads.config.UsefulRailroadsConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class SpeedClampRailBlock extends CustomPoweredRailBlock {
	
	@Override
	protected void controlSpeed(BlockPos pos, BlockState state, AbstractMinecartEntity cart) {
		setCartSpeed(cart, UsefulRailroadsConfig.SPEED_CLAMP_RAIL_SPEED.get());
	}
	
	@Override
	public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
	
}
