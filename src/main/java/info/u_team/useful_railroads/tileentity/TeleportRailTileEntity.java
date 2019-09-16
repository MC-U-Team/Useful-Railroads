package info.u_team.useful_railroads.tileentity;

import info.u_team.u_team_core.api.sync.IInitSyncedTileEntity;
import info.u_team.u_team_core.tileentity.UTileEntity;
import info.u_team.useful_railroads.container.TeleportRailContainer;
import info.u_team.useful_railroads.init.UsefulRailroadsTileEntityTypes;
import info.u_team.useful_railroads.util.Location;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.*;

public class TeleportRailTileEntity extends UTileEntity implements IInitSyncedTileEntity {
	
	private Location location = Location.ORIGIN;
	private int fuel;
	private int cost;
	
	private final LazyOptional<IItemHandler> slot = LazyOptional.of(() -> new TeleportRailItemHandler(() -> getWorld(), fuelAdder -> {
		fuel += fuelAdder;
		markDirty();
	}));
	
	public TeleportRailTileEntity() {
		super(UsefulRailroadsTileEntityTypes.TELEPORT_RAIL);
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
	
	public void teleport(World world, BlockPos pos, AbstractMinecartEntity cart) {
		if (world.isRemote) {
			return;
		}
		
		checkCost();
		final Entity entity = cart.getPassengers().isEmpty() ? null : (Entity) cart.getPassengers().get(0);
		
		if (fuel < cost) {
			if (entity instanceof PlayerEntity) {
				((PlayerEntity) entity).sendStatusMessage(new StringTextComponent(TextFormatting.DARK_RED + "Fuel missing! Need: " + TextFormatting.GOLD + cost + TextFormatting.DARK_RED + ". Have: " + TextFormatting.GOLD + fuel), true);
			}
			return;
		}
		fuel -= cost;
		markDirty();
		
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
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != Direction.UP) {
			return slot.cast();
		}
		return super.getCapability(capability, side);
	}
	
	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
		return new TeleportRailContainer(id, playerInventory, this);
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return new StringTextComponent("Teleport rail"); // TODO use translation
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
