package info.u_team.useful_railroads.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockRailIntersection extends BlockCustomRail {
	
	public BlockRailIntersection(String name) {
		super(name);
	}
	
	@Override
	public RailShape getRailDirection(BlockState state, IBlockReader world, BlockPos pos, AbstractMinecartEntity cart) {
		if (cart != null) {
			if (Math.abs(cart.getMotion().getX()) > 0) {
				return RailShape.EAST_WEST;
			} else if (Math.abs(cart.getMotion().getZ()) > 0) {
				return RailShape.NORTH_SOUTH;
			}
		}
		return super.getRailDirection(state, world, pos, cart);
	}
	
	@Override
	public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isFlexibleRail(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
}
