package info.u_team.useful_railroads.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.*;

public class HighSpeedRailBlock extends CustomPoweredRailBlock {
	
	public HighSpeedRailBlock(String name) {
		super(name);
	}
	
	@Override
	protected void controllSpeed(BlockPos pos, BlockState state, AbstractMinecartEntity cart) {
		double d21 = cart.isBeingRidden() ? 2.75D : 2.0D;
		double d22 = 5.4F;
		Vec3d vec3d1 = cart.getMotion();
		cart.move(MoverType.SELF, new Vec3d(MathHelper.clamp(d21 * vec3d1.x, -d22, d22), 0.0D, MathHelper.clamp(d21 * vec3d1.z, -d22, d22)));
	}
	
}
