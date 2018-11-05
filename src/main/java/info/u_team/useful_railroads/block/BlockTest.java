package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.api.IModelProvider;
import info.u_team.u_team_core.api.registry.IUBlock;
import info.u_team.useful_railroads.init.UsefulRailroadsCreativeTabs;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class BlockTest extends BlockRailPowered implements IUBlock, IModelProvider {
	
	public BlockTest() {
		setCreativeTab(UsefulRailroadsCreativeTabs.tab);
	}
	
	// @Override
	// public float getRailMaxSpeed(World world, EntityMinecart cart, BlockPos pos)
	// {
	// return 100F;
	// }
	
	@Override
	public void onMinecartPass(World world, EntityMinecart cart, BlockPos pos) {
		// if (!world.getBlockState(pos).getValue(POWERED))
		// return;
		
		// ReflectionHelper.setPrivateValue(EntityMinecart.class, cart, 100F,
		// "currentSpeedRail");
		
		// Only Air dont care
		// ReflectionHelper.setPrivateValue(EntityMinecart.class, cart, 1F,
		// "maxSpeedAirLateral");
		// ReflectionHelper.setPrivateValue(EntityMinecart.class, cart, -10F,
		// "maxSpeedAirVertical");
		
		// try {
		// Method method = EntityMinecart.class.getDeclaredMethod("getMaxSpeed");
		// method.setAccessible(true);
		// System.out.println(method.invoke(cart));
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		
		double d15 = Math.sqrt(cart.motionX * cart.motionX + cart.motionZ * cart.motionZ);
		
		if (d15 > 0.01D) {
			double d16 = 0.12D;
			cart.motionX += cart.motionX / d15 * d16;
			cart.motionZ += cart.motionZ / d15 * d16;
		}
		
		double mX = cart.motionX;
		double mZ = cart.motionZ;
		
		if (cart.isBeingRidden()) {
			mX *= 1.75D;
			mZ *= 1.75D;
		}
		
		// double max = 100;
		// mX = MathHelper.clamp(mX, -max, max);
		// mZ = MathHelper.clamp(mZ, -max, max);
		
		System.out.println(mX);
		System.out.println(mZ);
		cart.move(MoverType.SELF, mX, 0, mZ);
	}
	
	@Override
	public String getName() {
		return "test";
	}
	
	@Override
	public void registerModel() {
	}
	
	@Override
	public Item getItem() {
		return Item.getItemFromBlock(this);
	}
	
	@Override
	public ItemBlock getItemBlock() {
		return (ItemBlock) new ItemBlock(this).setRegistryName(getRegistryName());
	}
	
}
