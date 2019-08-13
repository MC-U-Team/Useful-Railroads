package info.u_team.useful_railroads.tileentity;

import info.u_team.u_team_core.tileentity.UTileEntity;
import info.u_team.useful_railroads.init.UsefulRailroadsTileEntities;
import info.u_team.useful_railroads.util.Location;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.*;

public class TeleportRailTileEntity extends UTileEntity implements INamedContainerProvider {
	
	private Location location = Location.ORIGIN;
	private int fuel;
	private int cost;
	
	private final LazyOptional<ItemStackHandler> slot = LazyOptional.of(() -> new ItemStackHandler(1) {
		
		protected void onContentsChanged(int slot) {
			if (world.isRemote) {
				return;
			}
			final ItemStack stack = getStackInSlot(slot); // Validate item with config value
			fuel += stack.getCount() * 100;
			setStackInSlot(slot, ItemStack.EMPTY);
		};
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
	public void sendChunkLoadData(CompoundNBT compound) {
		writeNBT(compound);
	}
	
	@Override
	public void handleChunkLoadData(CompoundNBT compound) {
		readNBT(compound);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return slot.cast();
		}
		return super.getCapability(capability, side);
	}
	
	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
		return null;
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return new StringTextComponent("Teleport rail"); // TODO use translation
	}
}
