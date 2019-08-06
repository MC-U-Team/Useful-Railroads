package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.api.registry.IUBlockRegistryType;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.init.UsefulRailroadsBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
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
		cart.fallDistance = 0.0F;
		Vec3d vec3d = cart.getPos(cart.posX, cart.posY, cart.posZ);
		cart.posY = (double) pos.getY();
		boolean flag = false;
		boolean flag1 = false;
		AbstractRailBlock abstractrailblock = (AbstractRailBlock) state.getBlock();
		if (abstractrailblock == UsefulRailroadsBlocks.HIGHSPEED_RAIL) {
			flag = state.get(PoweredRailBlock.POWERED);
			flag1 = !flag;
		}
		
		double d0 = cart.getSlopeAdjustment();
		Vec3d vec3d1 = cart.getMotion();
		RailShape railshape = ((AbstractRailBlock) state.getBlock()).getRailDirection(state, cart.world, pos, cart);
		switch (railshape) {
		case ASCENDING_EAST:
			cart.setMotion(vec3d1.add(-1 * d0, 0.0D, 0.0D));
			++cart.posY;
			break;
		case ASCENDING_WEST:
			cart.setMotion(vec3d1.add(d0, 0.0D, 0.0D));
			++cart.posY;
			break;
		case ASCENDING_NORTH:
			cart.setMotion(vec3d1.add(0.0D, 0.0D, d0));
			++cart.posY;
			break;
		case ASCENDING_SOUTH:
			cart.setMotion(vec3d1.add(0.0D, 0.0D, -1 * d0));
			++cart.posY;
		default:
			break;
		}
		
		final int[][][] MATRIX = new int[][][] { { { 0, 0, -1 }, { 0, 0, 1 } }, { { -1, 0, 0 }, { 1, 0, 0 } }, { { -1, -1, 0 }, { 1, 0, 0 } }, { { -1, 0, 0 }, { 1, -1, 0 } }, { { 0, 0, -1 }, { 0, -1, 1 } }, { { 0, -1, -1 }, { 0, 0, 1 } }, { { 0, 0, 1 }, { 1, 0, 0 } }, { { 0, 0, 1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { 1, 0, 0 } } };
		
		vec3d1 = cart.getMotion();
		int[][] aint = MATRIX[railshape.getMeta()];
		double d1 = (double) (aint[1][0] - aint[0][0]);
		double d2 = (double) (aint[1][2] - aint[0][2]);
		double d3 = Math.sqrt(d1 * d1 + d2 * d2);
		double d4 = vec3d1.x * d1 + vec3d1.z * d2;
		if (d4 < 0.0D) {
			d1 = -d1;
			d2 = -d2;
		}
		
		double d5 = Math.min(2.0D, Math.sqrt(Entity.func_213296_b(vec3d1)));
		vec3d1 = new Vec3d(d5 * d1 / d3, vec3d1.y, d5 * d2 / d3);
		cart.setMotion(vec3d1);
		Entity entity = cart.getPassengers().isEmpty() ? null : cart.getPassengers().get(0);
		if (entity instanceof PlayerEntity) {
			Vec3d vec3d2 = entity.getMotion();
			double d6 = Entity.func_213296_b(vec3d2);
			double d8 = Entity.func_213296_b(cart.getMotion());
			if (d6 > 1.0E-4D && d8 < 0.01D) {
				cart.setMotion(cart.getMotion().add(vec3d2.x * 0.1D, 0.0D, vec3d2.z * 0.1D));
				flag1 = false;
			}
		}
		
		if (flag1 && cart.shouldDoRailFunctions()) {
			double d19 = Math.sqrt(Entity.func_213296_b(cart.getMotion()));
			if (d19 < 0.03D) {
				cart.setMotion(Vec3d.ZERO);
			} else {
				cart.setMotion(cart.getMotion().mul(0.5D, 0.0D, 0.5D));
			}
		}
		
		double d20 = (double) pos.getX() + 0.5D + (double) aint[0][0] * 0.5D;
		double d7 = (double) pos.getZ() + 0.5D + (double) aint[0][2] * 0.5D;
		double d9 = (double) pos.getX() + 0.5D + (double) aint[1][0] * 0.5D;
		double d10 = (double) pos.getZ() + 0.5D + (double) aint[1][2] * 0.5D;
		d1 = d9 - d20;
		d2 = d10 - d7;
		double d11;
		if (d1 == 0.0D) {
			cart.posX = (double) pos.getX() + 0.5D;
			d11 = cart.posZ - (double) pos.getZ();
		} else if (d2 == 0.0D) {
			cart.posZ = (double) pos.getZ() + 0.5D;
			d11 = cart.posX - (double) pos.getX();
		} else {
			double d12 = cart.posX - d20;
			double d13 = cart.posZ - d7;
			d11 = (d12 * d1 + d13 * d2) * 2.0D;
		}
		
		cart.posX = d20 + d1 * d11;
		cart.posZ = d7 + d2 * d11;
		cart.setPosition(cart.posX, cart.posY, cart.posZ);
		cart.moveMinecartOnRail(pos);
		if (aint[0][1] != 0 && MathHelper.floor(cart.posX) - pos.getX() == aint[0][0] && MathHelper.floor(cart.posZ) - pos.getZ() == aint[0][2]) {
			cart.setPosition(cart.posX, cart.posY + (double) aint[0][1], cart.posZ);
		} else if (aint[1][1] != 0 && MathHelper.floor(cart.posX) - pos.getX() == aint[1][0] && MathHelper.floor(cart.posZ) - pos.getZ() == aint[1][2]) {
			cart.setPosition(cart.posX, cart.posY + (double) aint[1][1], cart.posZ);
		}
		
		// cart.applyDrag();
		
		double xd0 = cart.isBeingRidden() ? 0.997D : 0.96D;
		cart.setMotion(cart.getMotion().mul(xd0, 0.0D, xd0));
		
		Vec3d vec3d3 = cart.getPos(cart.posX, cart.posY, cart.posZ);
		if (vec3d3 != null && vec3d != null) {
			double d14 = (vec3d.y - vec3d3.y) * 0.05D;
			Vec3d vec3d4 = cart.getMotion();
			double d15 = Math.sqrt(Entity.func_213296_b(vec3d4));
			if (d15 > 0.0D) {
				cart.setMotion(vec3d4.mul((d15 + d14) / d15, 1.0D, (d15 + d14) / d15));
			}
			
			cart.setPosition(cart.posX, vec3d3.y, cart.posZ);
		}
		
		int j = MathHelper.floor(cart.posX);
		int i = MathHelper.floor(cart.posZ);
		if (j != pos.getX() || i != pos.getZ()) {
			Vec3d vec3d5 = cart.getMotion();
			double d23 = Math.sqrt(Entity.func_213296_b(vec3d5));
			cart.setMotion(d23 * (double) (j - pos.getX()), vec3d5.y, d23 * (double) (i - pos.getZ()));
		}
		
		// if (cart.shouldDoRailFunctions())
		// ((AbstractRailBlock) state.getBlock()).onMinecartPass(state, cart.world, pos,
		// cart);
		//
		if (flag && cart.shouldDoRailFunctions()) {
			Vec3d vec3d6 = cart.getMotion();
			double d24 = Math.sqrt(Entity.func_213296_b(vec3d6));
			if (d24 > 0.01D) {
				double d16 = 0.06D;
				cart.setMotion(vec3d6.add(vec3d6.x / d24 * 0.06D, 0.0D, vec3d6.z / d24 * 0.06D));
			} else {
				Vec3d vec3d7 = cart.getMotion();
				double d17 = vec3d7.x;
				double d18 = vec3d7.z;
				if (railshape == RailShape.EAST_WEST) {
					if (func_213900_a(cart.world, pos.west())) {
						d17 = 0.02D;
					} else if (func_213900_a(cart.world, pos.east())) {
						d17 = -0.02D;
					}
				} else {
					if (railshape != RailShape.NORTH_SOUTH) {
						return;
					}
					
					if (func_213900_a(cart.world, pos.north())) {
						d18 = 0.02D;
					} else if (func_213900_a(cart.world, pos.south())) {
						d18 = -0.02D;
					}
				}
				
				cart.setMotion(d17, vec3d7.y, d18);
			}
		}
		
	}
	
	private boolean func_213900_a(World world, BlockPos p_213900_1_) {
		return world.getBlockState(p_213900_1_).isNormalCube(world, p_213900_1_);
	}
}
