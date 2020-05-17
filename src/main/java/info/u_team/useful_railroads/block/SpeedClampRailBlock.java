package info.u_team.useful_railroads.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class SpeedClampRailBlock extends CustomPoweredRailBlock {
	
	public SpeedClampRailBlock(String name) {
		super(name);
	}
	
	@Override
	protected void controllSpeed(BlockPos pos, BlockState state, AbstractMinecartEntity cart) {
		speedUpCart(cart, 0.01D, 0.01D);
	}
	
	@Override
	public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
	
}
