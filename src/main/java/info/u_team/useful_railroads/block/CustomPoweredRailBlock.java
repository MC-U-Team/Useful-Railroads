package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.api.registry.IBlockItemProvider;
import info.u_team.useful_railroads.init.UsefulRailroadsItemGroups;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public abstract class CustomPoweredRailBlock extends PoweredRailBlock implements IBlockItemProvider {
	
	protected final BlockItem blockItem;
	
	protected final double minSpeed = 0.01D;
	
	public CustomPoweredRailBlock() {
		super(Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.7F).sound(SoundType.METAL), true);
		blockItem = createBlockItem(new Item.Properties().group(UsefulRailroadsItemGroups.GROUP));
	}
	
	protected BlockItem createBlockItem(Item.Properties blockItemProperties) {
		return new BlockItem(this, blockItemProperties);
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
		
		final boolean powered = (railBlock == this) ? state.get(PoweredRailBlock.POWERED) : false;
		
		final double currentSpeed = getPlaneSqrtDistance(cart.getMotion());
		if (currentSpeed < minSpeed) {
			if (cart.shouldDoRailFunctions() && powered) {
				doPushOffWall(pos, cart, railDirection, cart.getMotion());
			}
			doPassengerPush(cart);
		} else if (cart.shouldDoRailFunctions()) {
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
			final Vector3d passengerMotion = passenger.getMotion();
			final double passengerDistanceSqr = getPlaneSqrDistance(passengerMotion);
			
			if (passengerDistanceSqr > 1.0E-4D) {
				cart.setMotion(cart.getMotion().add(passengerMotion.x * 0.1D, 0.0D, passengerMotion.z * 0.1D));
			}
		}
	}
	
	protected void doUnpoweredMovement(AbstractMinecartEntity cart) {
		final double currentSpeed = getPlaneSqrtDistance(cart.getMotion());
		if (currentSpeed < minSpeed) {
			cart.setMotion(Vector3d.ZERO);
		} else {
			cart.setMotion(cart.getMotion().mul(0.5D, 0.0D, 0.5D));
		}
	}
	
	protected void doPoweredMovement(BlockPos pos, BlockState state, AbstractMinecartEntity cart, RailShape railDirection) {
		controlSpeed(pos, state, cart);
	}
	
	protected void doPushOffWall(BlockPos pos, AbstractMinecartEntity cart, RailShape railDirection, Vector3d cartMotion) {
		double xCartMotion = cartMotion.x;
		double zCartMotion = cartMotion.z;
		if (railDirection == RailShape.EAST_WEST) {
			if (isNormalCube(cart.world, pos.west())) {
				xCartMotion = minSpeed;
			} else if (isNormalCube(cart.world, pos.east())) {
				xCartMotion = -minSpeed;
			}
		} else if (railDirection == RailShape.NORTH_SOUTH) {
			if (isNormalCube(cart.world, pos.north())) {
				zCartMotion = minSpeed;
			} else if (isNormalCube(cart.world, pos.south())) {
				zCartMotion = -minSpeed;
			}
		} else {
			return;
		}
		cart.setMotion(xCartMotion, cartMotion.y, zCartMotion);
	}
	
	protected void controlSpeed(BlockPos pos, BlockState state, AbstractMinecartEntity cart) {
		final Vector3d cartMotion = cart.getMotion();
		final double cartDistance = getPlaneSqrtDistance(cartMotion);
		cart.setMotion(cartMotion.add(cartMotion.x / cartDistance * 0.06D, 0.0D, cartMotion.z / cartDistance * 0.06D));
	}
	
	protected void speedUpCart(AbstractMinecartEntity cart, double accel, double speedClamp) {
		final Vector3d motion = cart.getMotion();
		final double speed = motion.length();
		
		final double newSpeed = MathHelper.clamp(speed + accel, -speedClamp, speedClamp);
		
		setCartSpeed(cart, newSpeed, motion);
	}
	
	protected void setCartSpeed(AbstractMinecartEntity cart, double speed) {
		setCartSpeed(cart, speed, cart.getMotion());
	}
	
	protected void setCartSpeed(AbstractMinecartEntity cart, double speed, Vector3d direction) {
		final Vector3d directionNormalised = direction.normalize(); // in case not already normalised
		setCartSpeed(cart, directionNormalised.mul(new Vector3d(speed, speed, speed)));
	}
	
	protected void setCartSpeed(AbstractMinecartEntity cart, double velX, double velY, double velZ) {
		setCartSpeed(cart, new Vector3d(velX, velY, velZ));
	}
	
	protected void setCartSpeed(AbstractMinecartEntity cart, Vector3d vel) {
		// set motion manually before calling move to override some vanilla behaviour
		cart.setMotion(vel);
		cart.move(MoverType.SELF, vel);
	}
	
	private static boolean isNormalCube(World world, BlockPos pos) {
		return world.getBlockState(pos).isNormalCube(world, pos);
	}
	
	private static double getPlaneSqrtDistance(Vector3d vec) {
		return Math.sqrt(getPlaneSqrDistance(vec));
	}
	
	private static double getPlaneSqrDistance(Vector3d vec) {
		return vec.x * vec.x + vec.z * vec.z;
	}
}
