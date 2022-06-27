package info.u_team.useful_railroads.block;

import info.u_team.useful_railroads.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class SpeedClampRailBlock extends CustomPoweredRailBlock {
	
	@Override
	protected void controlSpeed(BlockPos pos, BlockState state, AbstractMinecart cart) {
		setCartSpeed(cart, CommonConfig.getInstance().speedClampRailSpeed.get());
	}
	
	@Override
	public boolean canMakeSlopes(BlockState state, BlockGetter level, BlockPos pos) {
		return false;
	}
	
}
