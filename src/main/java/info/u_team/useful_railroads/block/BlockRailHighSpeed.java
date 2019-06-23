package info.u_team.useful_railroads.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class BlockRailHighSpeed extends BlockCustomRailPowered {
	
	public BlockRailHighSpeed(String name) {
		super(name);
	}
	
	@Override
	public void onMinecartPassPowered(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		
		double sqrt = Math.sqrt(cart.getMotion().getX() * cart.getMotion().getX() + cart.getMotion().getZ() * cart.getMotion().getZ());
		
		if (sqrt > 0.01) {
			double multiplier = 0.06;
			cart.setMotion(cart.getMotion().add(cart.getMotion().getX() / sqrt * multiplier, 0, cart.getMotion().getZ() / sqrt * multiplier));
		}
		
		double mX = cart.getMotion().getX();
		double mZ = cart.getMotion().getZ();
		
		if (cart.isBeingRidden()) {
			mX *= 4;
			mZ *= 4;
		}
		
		mX = MathHelper.clamp(mX, -5, 5);
		mZ = MathHelper.clamp(mZ, -5, 5);
		
		cart.move(MoverType.SELF, new Vec3d(mX, 0, mZ));
	}
}
