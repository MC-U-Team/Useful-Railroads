package info.u_team.useful_railroads.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;

public class HighSpeedRailBlock extends CustomPoweredRailBlock {
	
	public HighSpeedRailBlock(String name) {
		super(name);
	}
	
	@Override
	protected void controllSpeed(BlockPos pos, BlockState state, AbstractMinecartEntity cart) {
		speedUpCart(cart, cart.isBeingRidden() ? 4 : 2, 5);
	}
	
}
