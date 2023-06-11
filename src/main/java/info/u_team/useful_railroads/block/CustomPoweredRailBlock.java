package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.api.block.BlockItemProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

public abstract class CustomPoweredRailBlock extends PoweredRailBlock implements BlockItemProvider {
	
	protected final BlockItem blockItem;
	
	protected final double minSpeed = 0.01D;
	
	public CustomPoweredRailBlock() {
		super(Properties.of().noCollission().strength(0.7F).sound(SoundType.METAL), true);
		blockItem = createBlockItem(new Item.Properties());
	}
	
	protected BlockItem createBlockItem(Item.Properties blockItemProperties) {
		return new BlockItem(this, blockItemProperties);
	}
	
	@Override
	public BlockItem blockItem() {
		return blockItem;
	}
	
	@Override
	public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
		moveAlongTrack(pos, state, cart);
	}
	
	protected void moveAlongTrack(BlockPos pos, BlockState state, AbstractMinecart cart) {
		final BaseRailBlock railBlock = (BaseRailBlock) state.getBlock();
		final RailShape railDirection = railBlock.getRailDirection(state, cart.level(), pos, cart);
		
		final boolean powered = (railBlock == this) ? state.getValue(PoweredRailBlock.POWERED) : false;
		
		final double currentSpeed = getPlaneSqrtDistance(cart.getDeltaMovement());
		if (currentSpeed < minSpeed) {
			if (cart.shouldDoRailFunctions() && powered) {
				doPushOffWall(pos, cart, railDirection, cart.getDeltaMovement());
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
	
	protected void doPassengerPush(AbstractMinecart cart) {
		final Entity passenger = cart.getPassengers().isEmpty() ? null : cart.getPassengers().get(0);
		if (passenger instanceof Player) {
			final Vec3 passengerMotion = passenger.getDeltaMovement();
			final double passengerDistanceSqr = getPlaneSqrDistance(passengerMotion);
			
			if (passengerDistanceSqr > 1.0E-4D) {
				cart.setDeltaMovement(cart.getDeltaMovement().add(passengerMotion.x * 0.1D, 0.0D, passengerMotion.z * 0.1D));
			}
		}
	}
	
	protected void doUnpoweredMovement(AbstractMinecart cart) {
		final double currentSpeed = getPlaneSqrtDistance(cart.getDeltaMovement());
		if (currentSpeed < minSpeed) {
			cart.setDeltaMovement(Vec3.ZERO);
		} else {
			cart.setDeltaMovement(cart.getDeltaMovement().multiply(0.5D, 0.0D, 0.5D));
		}
	}
	
	protected void doPoweredMovement(BlockPos pos, BlockState state, AbstractMinecart cart, RailShape railDirection) {
		controlSpeed(pos, state, cart);
	}
	
	protected void doPushOffWall(BlockPos pos, AbstractMinecart cart, RailShape railDirection, Vec3 cartMotion) {
		double xCartMotion = cartMotion.x;
		double zCartMotion = cartMotion.z;
		if (railDirection == RailShape.EAST_WEST) {
			if (isNormalCube(cart.level(), pos.west())) {
				xCartMotion = minSpeed;
			} else if (isNormalCube(cart.level(), pos.east())) {
				xCartMotion = -minSpeed;
			}
		} else if (railDirection == RailShape.NORTH_SOUTH) {
			if (isNormalCube(cart.level(), pos.north())) {
				zCartMotion = minSpeed;
			} else if (isNormalCube(cart.level(), pos.south())) {
				zCartMotion = -minSpeed;
			}
		} else {
			return;
		}
		cart.setDeltaMovement(xCartMotion, cartMotion.y, zCartMotion);
	}
	
	protected void controlSpeed(BlockPos pos, BlockState state, AbstractMinecart cart) {
		final Vec3 cartMotion = cart.getDeltaMovement();
		final double cartDistance = getPlaneSqrtDistance(cartMotion);
		cart.setDeltaMovement(cartMotion.add(cartMotion.x / cartDistance * 0.06D, 0.0D, cartMotion.z / cartDistance * 0.06D));
	}
	
	protected void speedUpCart(AbstractMinecart cart, double accel, double speedClamp) {
		final Vec3 motion = cart.getDeltaMovement();
		final double speed = motion.length();
		
		final double newSpeed = Mth.clamp(speed + accel, -speedClamp, speedClamp);
		
		setCartSpeed(cart, newSpeed, motion);
	}
	
	protected void setCartSpeed(AbstractMinecart cart, double speed) {
		setCartSpeed(cart, speed, cart.getDeltaMovement());
	}
	
	protected void setCartSpeed(AbstractMinecart cart, double speed, Vec3 direction) {
		final Vec3 directionNormalised = direction.normalize(); // in case not already normalised
		setCartSpeed(cart, directionNormalised.multiply(new Vec3(speed, speed, speed)));
	}
	
	protected void setCartSpeed(AbstractMinecart cart, double velX, double velY, double velZ) {
		setCartSpeed(cart, new Vec3(velX, velY, velZ));
	}
	
	protected void setCartSpeed(AbstractMinecart cart, Vec3 vel) {
		// set motion manually before calling move to override some vanilla behaviour
		cart.setDeltaMovement(vel);
		cart.move(MoverType.SELF, vel);
	}
	
	private static boolean isNormalCube(Level level, BlockPos pos) {
		return level.getBlockState(pos).isRedstoneConductor(level, pos);
	}
	
	private static double getPlaneSqrtDistance(Vec3 vec) {
		return Math.sqrt(getPlaneSqrDistance(vec));
	}
	
	private static double getPlaneSqrDistance(Vec3 vec) {
		return vec.x * vec.x + vec.z * vec.z;
	}
}
