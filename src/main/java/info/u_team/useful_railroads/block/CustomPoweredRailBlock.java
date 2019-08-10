package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.api.registry.IUBlockRegistryType;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.init.UsefulRailroadsBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID)
public class CustomPoweredRailBlock extends PoweredRailBlock implements IUBlockRegistryType {
	
	public CustomPoweredRailBlock(String string) {
		super(Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.7F).sound(SoundType.METAL), true);
	}
	
	@Override
	public String getEntryName() {
		return "rail_highspeed";
	}
	
	@Override
	public BlockItem getBlockItem() {
		return new BlockItem(this, new Item.Properties());
	}
	
	@Override
	public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		// if (!state.get(POWERED)) {
		// cart.setMotion(0, 0, 0);
		// return;
		// }
		onMinecartPassPowered(state, world, pos, cart);
	}
	
	public void onMinecartPassPowered(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		moveAlongTrack(pos, state, cart);
	}
	
	protected void moveAlongTrack(BlockPos pos, BlockState state, AbstractMinecartEntity cart) {
//		boolean flag = false;
//		boolean flag1 = false;
//		AbstractRailBlock abstractrailblock = (AbstractRailBlock) state.getBlock();
//		if (abstractrailblock == UsefulRailroadsBlocks.HIGHSPEED_RAIL) {
//			flag = state.get(PoweredRailBlock.POWERED);
//			flag1 = !flag;
//		}
//		
//		RailShape railshape = ((AbstractRailBlock) state.getBlock()).getRailDirection(state, cart.world, pos, cart);
//		Entity entity = cart.getPassengers().isEmpty() ? null : cart.getPassengers().get(0);
//		if (entity instanceof PlayerEntity) {
//			Vec3d vec3d2 = entity.getMotion();
//			double d6 = Entity.func_213296_b(vec3d2);
//			double d8 = Entity.func_213296_b(cart.getMotion());
//			if (d6 > 1.0E-4D && d8 < 0.01D) {
//				cart.setMotion(cart.getMotion().add(vec3d2.x * 0.1D, 0.0D, vec3d2.z * 0.1D));
//				flag1 = false;
//			}
//		}
//		
//		if (flag1 && cart.shouldDoRailFunctions()) {
//			double d19 = Math.sqrt(Entity.func_213296_b(cart.getMotion()));
//			if (d19 < 0.03D) {
//				cart.setMotion(Vec3d.ZERO);
//			} else {
//				cart.setMotion(cart.getMotion().mul(0.5D, 0.0D, 0.5D));
//			}
//		}
//		
//		if (flag && cart.shouldDoRailFunctions()) {
//			Vec3d vec3d6 = cart.getMotion();
//			double d24 = Math.sqrt(Entity.func_213296_b(vec3d6));
//			if (d24 > 0.01D) {
//				double d16 = 0.06D;
//				cart.setMotion(vec3d6.add(vec3d6.x / d24 * d16, 0.0D, vec3d6.z / d24 * d16));
//				
				   double d21 = cart.isBeingRidden() ? 2.75D : 2.0D;
				      double d22 = 5.4F;
				      Vec3d vec3d1 = cart.getMotion();
				      cart.move(MoverType.SELF, new Vec3d(MathHelper.clamp(d21 * vec3d1.x, -d22, d22), 0.0D, MathHelper.clamp(d21 * vec3d1.z, -d22, d22)));
				
//				cart.moveMinecartOnRail(pos);
//			} else {
//				Vec3d vec3d7 = cart.getMotion();
//				double d17 = vec3d7.x;
//				double d18 = vec3d7.z;
//				if (railshape == RailShape.EAST_WEST) {
//					if (func_213900_a(cart.world, pos.west())) {
//						d17 = 0.02D;
//					} else if (func_213900_a(cart.world, pos.east())) {
//						d17 = -0.02D;
//					}
//				} else {
//					if (railshape != RailShape.NORTH_SOUTH) {
//						return;
//					}
//					
//					if (func_213900_a(cart.world, pos.north())) {
//						d18 = 0.02D;
//					} else if (func_213900_a(cart.world, pos.south())) {
//						d18 = -0.02D;
//					}
//				}
//				
//				cart.setMotion(d17, vec3d7.y, d18);
//			}
//		}
	}
	
	private boolean func_213900_a(World world, BlockPos p_213900_1_) {
		return world.getBlockState(p_213900_1_).isNormalCube(world, p_213900_1_);
	}
}
