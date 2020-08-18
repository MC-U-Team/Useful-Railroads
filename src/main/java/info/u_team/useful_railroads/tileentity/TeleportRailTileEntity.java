package info.u_team.useful_railroads.tileentity;

import info.u_team.u_team_core.api.sync.IInitSyncedTileEntity;
import info.u_team.u_team_core.tileentity.UTileEntity;
import info.u_team.u_team_core.util.world.WorldUtil;
import info.u_team.useful_railroads.container.TeleportRailContainer;
import info.u_team.useful_railroads.init.*;
import info.u_team.useful_railroads.inventory.FuelItemHandler;
import info.u_team.useful_railroads.recipe.TeleportRailFuelRecipe;
import info.u_team.useful_railroads.util.Location;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.server.ServerWorld;
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
		
		if (location.getDimensionType() != world.getDimension().getType()) {
			calculatedCost += 100;
		}
		double calculatedDistance = Math.log(pos.distanceSq(location.getPos())) / Math.log(5);
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
				((PlayerEntity) entity).sendStatusMessage(new TranslationTextComponent("block.usefulrailroads.teleport_rail.not_enough_fuel", cost).setStyle(new Style().setColor(TextFormatting.RED)), true);
			}
			return;
		}
		fuel -= cost;
		markDirty();
		
		// Teleportation process
		final ServerWorld teleportWorld = cart.getServer().getWorld(location.getDimensionType());
		
		// Enqueue the teleportation to be executed after the ticks of entites because
		// else the teleportation will crash
		cart.getServer().enqueue(new TickDelayedTask(0, () -> {
			final Vec3d teleportPos = new Vec3d(location.getPos()).add(0.5, 0, 0.5);
			
			// Teleport entity riding if there is one
			if (entity != null) {
				entity.detach(); // Detach entity
				WorldUtil.teleportEntity(entity, teleportWorld, teleportPos);
			}
			
			// Teleport minecart
			WorldUtil.teleportEntity(cart, teleportWorld, teleportPos);
			// Reatach entity
			if (entity != null) {
				// Because the entity will be destroyed when changing dimensions we use the uuid
				entity.startRiding(teleportWorld.getEntityByUuid(cart.getUniqueID()), true);
			}
		}));
		
	}
	
	@Override
	public void writeNBT(CompoundNBT compound) {
		compound.put("location", location.serializeNBT());
		if (fuel != 0) { // Don't save fuel if it's 0
			compound.putInt("fuel", fuel);
		}
	}
	
	@Override
	public void readNBT(CompoundNBT compound) {
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
