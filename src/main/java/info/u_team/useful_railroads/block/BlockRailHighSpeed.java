package info.u_team.useful_railroads.block;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class BlockRailHighSpeed extends BlockCustomRailPowered {
	
	public BlockRailHighSpeed(String name) {
		super(name);
	}
	
	@Override
	public void onMinecartPassPowered(World world, EntityMinecart cart, BlockPos pos) {
		double sqrt = Math.sqrt(cart.motionX * cart.motionX + cart.motionZ * cart.motionZ);
		
		if (sqrt > 0.01) {
			double multiplier = 0.06;
			cart.motionX += cart.motionX / sqrt * multiplier;
			cart.motionZ += cart.motionZ / sqrt * multiplier;
		}
		
		double mX = cart.motionX;
		double mZ = cart.motionZ;
		
		if (cart.isBeingRidden()) {
			mX *= 4;
			mZ *= 4;
		}
		
		mX = MathHelper.clamp(mX, -8, 8);
		mZ = MathHelper.clamp(mZ, -8, 8);
		
		cart.move(MoverType.SELF, mX, 0, mZ);
	}
	
}
