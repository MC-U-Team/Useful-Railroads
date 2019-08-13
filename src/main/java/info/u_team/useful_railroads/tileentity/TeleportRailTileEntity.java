package info.u_team.useful_railroads.tileentity;

import info.u_team.u_team_core.api.sync.IInitSyncedTileEntity;
import info.u_team.u_team_core.tileentity.UTileEntity;
import info.u_team.useful_railroads.container.TeleportRailContainer;
import info.u_team.useful_railroads.init.UsefulRailroadsTileEntities;
import info.u_team.useful_railroads.util.Location;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.*;

public class TeleportRailTileEntity extends UTileEntity implements IInitSyncedTileEntity {
	
	private Location location = Location.ORIGIN;
	private int fuel;
	private int cost;
	
	private final LazyOptional<IItemHandler> slot = LazyOptional.of(() -> new IItemHandlerModifiable() {
		
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return stack.getItem() == Items.DIAMOND;
		}
		
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (stack.isEmpty()) {
				return ItemStack.EMPTY;
			}
			
			if (!isItemValid(slot, stack)) {
				return stack;
			}
			if (!simulate) {
				setStackInSlot(slot, stack);
			}
			return ItemStack.EMPTY;
		}
		
		@Override
		public ItemStack getStackInSlot(int slot) {
			return ItemStack.EMPTY;
		}
		
		@Override
		public int getSlots() {
			return 1;
		}
		
		@Override
		public int getSlotLimit(int slot) {
			return 64;
		}
		
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			return ItemStack.EMPTY;
		}
		
		@Override
		public void setStackInSlot(int slot, ItemStack stack) {
			if (!world.isRemote) {
				fuel += 100 * stack.getCount();
			}
		}
	});
	
	public TeleportRailTileEntity() {
		super(UsefulRailroadsTileEntities.TELEPORT_RAIL);
	}
	
	@Override
	public void writeNBT(CompoundNBT compound) {
		compound.put("location", location.serializeNBT());
		compound.putInt("fuel", fuel);
		compound.putInt("cost", cost);
	}
	
	@Override
	public void readNBT(CompoundNBT compound) {
		location.deserializeNBT(compound.getCompound("location"));
		fuel = compound.getInt("fuel");
		cost = compound.getInt("cost");
	}
	
	@Override
	public void sendInitialDataBuffer(PacketBuffer buffer) {
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
