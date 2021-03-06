package info.u_team.useful_railroads.block;

import info.u_team.useful_railroads.config.CommonConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class SpeedClampRailBlock extends CustomPoweredRailBlock {
	
	@Override
	protected void controlSpeed(BlockPos pos, BlockState state, AbstractMinecartEntity cart) {
		setCartSpeed(cart, CommonConfig.getInstance().speedClampRailSpeed.get());
	}
	
	@Override
	public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
	
}
