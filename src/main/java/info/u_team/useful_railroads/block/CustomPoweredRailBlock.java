package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.api.registry.IUBlockRegistryType;
import info.u_team.useful_railroads.init.UsefulRailroadsItemGroups;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public abstract class CustomPoweredRailBlock extends PoweredRailBlock implements IUBlockRegistryType {
	
	protected final String name;
	
	protected final BlockItem blockItem;
	
	public CustomPoweredRailBlock(String name) {
		super(Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.7F).sound(SoundType.METAL), true);
		this.name = name + "_rail";
		blockItem = createBlockItem(new Item.Properties().group(UsefulRailroadsItemGroups.GROUP));
	}
	
	protected BlockItem createBlockItem(Item.Properties blockItemProperties) {
		return new BlockItem(this, blockItemProperties);
	}
	
	@Override
	public String getEntryName() {
		return name;
	}
	
	@Override
	public BlockItem getBlockItem() {
		return blockItem;
	}
	
	@Override
	public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		moveAlongTrack(pos, state, cart);
	}
	
	protected void moveAlongTrack(BlockPos pos, BlockState state, AbstractMinecartEntity cart) {
		final AbstractRailBlock railBlock = (AbstractRailBlock) state.getBlock();
		final RailShape railDirection = railBlock.getRailDirection(state, cart.world, pos, cart);

		boolean powered = (railBlock == this) ? state.get(PoweredRailBlock.POWERED) : false;

		final double cartDistanceSqr = getPlaneSqrDistance(cart.getMotion());
		if (cartDistanceSqr < 0.01D) {
			doPassengerPush(cart);
			return;
		}
		if (cart.shouldDoRailFunctions()) {
			if (powered) {
				doPoweredMovement(pos, state, cart, railDirection);
			} else {
				doUnpoweredMovement(cart);
			}
		}
	}

	protected void doPassengerPush(AbstractMinecartEntity cart) {
		final Entity passenger = cart.getPassengers().isEmpty() ? null : cart.getPassengers().get(0);
		if (passenger instanceof PlayerEntity) {
			final Vec3d passengerMotion = passenger.getMotion();
			final double passengerDistanceSqr = getPlaneSqrDistance(passengerMotion);

			if (passengerDistanceSqr > 1.0E-4D) {
				cart.setMotion(cart.getMotion().add(passengerMotion.x * 0.1D, 0.0D, passengerMotion.z * 0.1D));
			}
		}
	}

	protected void doUnpoweredMovement(AbstractMinecartEntity cart) {
		final double cartDistance = getPlaneSqrtDistance(cart.getMotion());
		if (cartDistance < 0.03D) {
			cart.setMotion(Vec3d.ZERO);
		} else {
			cart.setMotion(cart.getMotion().mul(0.5D, 0.0D, 0.5D));
		}
	}

	protected void doPoweredMovement(BlockPos pos, BlockState state, AbstractMinecartEntity cart, RailShape railDirection) {
		final Vec3d cartMotion = cart.getMotion();
		final double cartDistance = getPlaneSqrtDistance(cartMotion);
		if (cartDistance > 0.01D) {
			controlSpeed(pos, state, cart);
		} else {
			doPushOffWall(pos, cart, railDirection, cartMotion);
		}
	}

	protected void doPushOffWall(BlockPos pos, AbstractMinecartEntity cart, RailShape railDirection, Vec3d cartMotion) {
		double xCartMotion = cartMotion.x;
		double zCartMotion = cartMotion.z;
		if (railDirection == RailShape.EAST_WEST) {
			if (isNormalCube(cart.world, pos.west())) {
				xCartMotion = 0.02D;
			} else if (isNormalCube(cart.world, pos.east())) {
				xCartMotion = -0.02D;
			}
		} else if (railDirection == RailShape.NORTH_SOUTH) {
			if (isNormalCube(cart.world, pos.north())) {
				zCartMotion = 0.02D;
			} else if (isNormalCube(cart.world, pos.south())) {
				zCartMotion = -0.02D;
			}
		} else {
			return;
		}
		cart.setMotion(xCartMotion, cartMotion.y, zCartMotion);
	}

	protected void controlSpeed(BlockPos pos, BlockState state, AbstractMinecartEntity cart) {
		final Vec3d cartMotion = cart.getMotion();
		final double cartDistance = getPlaneSqrtDistance(cartMotion);
		cart.setMotion(cartMotion.add(cartMotion.x / cartDistance * 0.06D, 0.0D, cartMotion.z / cartDistance * 0.06D));
	}
	
	protected void speedUpCart(AbstractMinecartEntity cart, double speedMultiplier, double speedClamp) {
		final Vec3d motion = cart.getMotion();
		// set motion manually before calling move to override some vanilla behaviour
		cart.setMotion(new Vec3d(MathHelper.clamp(speedMultiplier * motion.x, -speedClamp, speedClamp), 0.0D, MathHelper.clamp(speedMultiplier * motion.z, -speedClamp, speedClamp)));
		cart.move(MoverType.SELF, new Vec3d(MathHelper.clamp(speedMultiplier * motion.x, -speedClamp, speedClamp), 0.0D, MathHelper.clamp(speedMultiplier * motion.z, -speedClamp, speedClamp)));
	}

	protected void setCartSpeed(AbstractMinecartEntity cart, double speed) {
		final Vec3d direction = cart.getMotion().normalize();
		// set motion manually before calling move to override some vanilla behaviour
		cart.setMotion(
				direction.getX() * speed,
				direction.getY() * speed,
				direction.getZ() * speed
		);
		cart.move(MoverType.SELF, new Vec3d(
				direction.getX() * speed,
				direction.getY() * speed,
				direction.getZ() * speed
		));
	}
	
	private static boolean isNormalCube(World world, BlockPos pos) {
		return world.getBlockState(pos).isNormalCube(world, pos);
	}
	
	private static double getPlaneSqrtDistance(Vec3d vec) {
		return Math.sqrt(getPlaneSqrDistance(vec));
	}
	
	private static double getPlaneSqrDistance(Vec3d vec) {
		return vec.x * vec.x + vec.z * vec.z;
	}
}
