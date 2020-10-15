package info.u_team.useful_railroads.tileentity;

import info.u_team.u_team_core.api.sync.IInitSyncedTileEntity;
import info.u_team.u_team_core.tileentity.UTileEntity;
import info.u_team.useful_railroads.config.CommonConfig;
import info.u_team.useful_railroads.container.TeleportRailContainer;
import info.u_team.useful_railroads.init.*;
import info.u_team.useful_railroads.inventory.FuelItemHandler;
import info.u_team.useful_railroads.recipe.TeleportRailFuelRecipe;
import info.u_team.useful_railroads.util.Location;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetPassengersPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.*;
import net.minecraft.world.server.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class TeleportRailTileEntity extends UTileEntity implements IInitSyncedTileEntity {
	
	private final Location location = Location.getOrigin();
	
	private int fuel;
	private int cost;
	
	private final FuelItemHandler<TeleportRailFuelRecipe> fuelSlot = new FuelItemHandler<>(UsefulRailroadsRecipeTypes.TELEPORT_RAIL_FUEL, this::getWorld, () -> fuel < 10000, fuelAdder -> {
		fuel += fuelAdder;
		markDirty();
	});
	
	private final LazyOptional<FuelItemHandler<TeleportRailFuelRecipe>> fuelSlotOptional = LazyOptional.of(() -> fuelSlot);
	
	public TeleportRailTileEntity() {
		super(UsefulRailroadsTileEntityTypes.TELEPORT_RAIL.get());
	}
	
	private void checkCost() {
		if (cost == 0) { // Calculate cost if not already done
			cost = calculateCost();
		}
	}
	
	private int calculateCost() {
		int calculatedCost = 0;
		
		if (!location.getRegistryKey().equals(world.getDimensionKey())) {
			calculatedCost += CommonConfig.getInstance().teleportRailDimensionCost.get();
		}
		double calculatedDistance = Math.log(pos.distanceSq(location.getPos())) / Math.log(CommonConfig.getInstance().teleportRailLogDivisionCost.get());
		calculatedDistance = calculatedDistance * calculatedDistance;
		calculatedCost += MathHelper.floor(calculatedDistance);
		if (calculatedCost == 0) {
			return 1;
		}
		return calculatedCost;
	}
	
	public void teleport(BlockPos pos, AbstractMinecartEntity cart) {
		checkCost();
		
		// Reset motion
		cart.setMotion(0, 0, 0);
		
		final Entity entity = cart.getPassengers().isEmpty() ? null : (Entity) cart.getPassengers().get(0);
		
		// Check fuel
		if (fuel < cost) {
			if (entity instanceof PlayerEntity) {
				((PlayerEntity) entity).sendStatusMessage(new TranslationTextComponent("block.usefulrailroads.teleport_rail.not_enough_fuel", cost).mergeStyle(TextFormatting.RED), true);
			}
			return;
		}
		fuel -= cost;
		markDirty();
		
		// Teleportation process
		final ServerWorld teleportWorld = cart.getServer().getWorld(location.getRegistryKey());
		
		if (teleportWorld == null) {
			return;
		}
		
		// Enqueue the teleportation to be executed after the ticks of entites because
		// else the teleportation will crash
		cart.getServer().enqueue(new TickDelayedTask(0, () -> {
			final Vector3d teleportPos = Vector3d.copyCentered(location.getPos());
			
			// Teleport minecart
			teleportEntity(cart, teleportWorld, teleportPos);
			
			// Teleport entity riding if there is one
			if (entity != null) {
				teleportEntity(entity, teleportWorld, teleportPos);
				
				// Reatach entity because the entity will be destroyed when changing dimensions we use the uuid
				final Entity newCart = teleportWorld.getEntityByUuid(cart.getUniqueID());
				entity.startRiding(newCart, true);
				
				// For some reason the entity tracker does not update the passengers to the client so we send the packet manually.
				// See MC-U-Team/Useful-Railroads#21
				teleportWorld.getChunkProvider().sendToTrackingAndSelf(entity, new SSetPassengersPacket(newCart));
			}
		}));
		
	}
	
	public static void teleportEntity(Entity entity, ServerWorld world, Vector3d pos) {
		teleportEntity(entity, world, pos.getX(), pos.getY(), pos.getZ(), entity.rotationYaw, entity.rotationPitch);
	}
	
	public static void teleportEntity(Entity entity, ServerWorld world, double x, double y, double z, float yaw, float pitch) {
		teleportEntity(entity, world, x, y, z, yaw, pitch, true);
	}
	
	public static void teleportEntity(Entity entity, ServerWorld world, double x, double y, double z, float yaw, float pitch, boolean detach) {
		if (entity instanceof ServerPlayerEntity) {
			final ServerPlayerEntity player = (ServerPlayerEntity) entity;
			world.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, new ChunkPos(new BlockPos(x, y, z)), 1, entity.getEntityId());
			if (detach) {
				player.stopRiding();
			}
			if (player.isSleeping()) {
				player.stopSleepInBed(true, true);
			}
			if (world == entity.world) {
				player.connection.setPlayerLocation(x, y, z, yaw, pitch);
			} else {
				player.teleport(world, x, y, z, yaw, pitch);
			}
			entity.setRotationYawHead(yaw);
		} else {
			final float wrapedYaw = MathHelper.wrapDegrees(yaw);
			final float wrapedPitch = MathHelper.clamp(MathHelper.wrapDegrees(pitch), -90.0F, 90.0F);
			if (world == entity.world) {
				entity.setLocationAndAngles(x, y, z, wrapedYaw, wrapedPitch);
				entity.setRotationYawHead(wrapedYaw);
			} else {
				if (detach) {
					entity.detach();
				}
				final Entity entityOld = entity;
				entity = entity.getType().create(world);
				if (entity == null) {
					return;
				}
				entity.copyDataFromOld(entityOld);
				// Need to remove the old entity (Why the heck does TeleportCommand don't do
				// this and it works ?????)
				entityOld.remove(false);
				entity.setLocationAndAngles(x, y, z, wrapedYaw, wrapedPitch);
				entity.setRotationYawHead(wrapedYaw);
				world.addFromAnotherDimension(entity);
			}
		}
		
		if (!(entity instanceof LivingEntity) || !((LivingEntity) entity).isElytraFlying()) {
			entity.setMotion(entity.getMotion().mul(1, 0, 1));
			entity.setOnGround(true);
		}
		
		if (entity instanceof CreatureEntity) {
			((CreatureEntity) entity).getNavigator().clearPath();
		}
	}
	
	@Override
	public void writeNBT(CompoundNBT compound) {
		compound.put("location", location.serializeNBT());
		if (fuel != 0) { // Don't save fuel if it's 0
			compound.putInt("fuel", fuel);
		}
	}
	
	@Override
	public void readNBT(BlockState state, CompoundNBT compound) {
		location.deserializeNBT(compound.getCompound("location"));
		fuel = compound.getInt("fuel");
	}
	
	@Override
	public void sendInitialDataBuffer(PacketBuffer buffer) {
		checkCost();
		location.serialize(buffer);
		buffer.writeInt(fuel);
		buffer.writeInt(cost);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleInitialDataBuffer(PacketBuffer buffer) {
		location.deserialize(buffer);
		fuel = buffer.readInt();
		cost = buffer.readInt();
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction direction) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && direction != Direction.UP) {
			return fuelSlotOptional.cast();
		}
		return super.getCapability(capability, direction);
	}
	
	@Override
	public void remove() {
		super.remove();
		fuelSlotOptional.invalidate();
	}
	
	public FuelItemHandler<TeleportRailFuelRecipe> getFuelSlot() {
		return fuelSlot;
	}
	
	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
		return new TeleportRailContainer(id, playerInventory, this);
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.usefulrailroads.teleport_rail");
	}
	
	public int getFuel() {
		return fuel;
	}
	
	public void setFuel(int fuel) {
		this.fuel = fuel;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public int getCost() {
		return cost;
	}
}
