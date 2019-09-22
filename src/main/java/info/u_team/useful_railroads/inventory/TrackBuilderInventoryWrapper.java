package info.u_team.useful_railroads.inventory;

import java.util.function.Supplier;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.items.*;

public class TrackBuilderInventoryWrapper {
	
	public static final int RAIL_INVENTORY_SIZE = 9;
	public static final int GROUND_BLOCK_INVENTORY_SIZE = 27;
	public static final int REDSTONE_TORCH_SIZE = 2;
	
	protected final ItemStackHandler railInventory = new ItemStackHandler(RAIL_INVENTORY_SIZE);
	protected final ItemStackHandler groundBlockInventory = new ItemStackHandler(GROUND_BLOCK_INVENTORY_SIZE);
	protected final ItemStackHandler redstoneTorchInventory = new ItemStackHandler(REDSTONE_TORCH_SIZE);
	protected final IItemHandler fuelInventory;
	
	protected int fuel;
	
	private TrackBuilderInventoryWrapper(Supplier<World> worldSupplier) {
		fuelInventory = new TrackBuilderFuelItemHandler(worldSupplier, fuelAdder -> fuel += fuelAdder);
	}
	
	public ItemStackHandler getRailInventory() {
		return railInventory;
	}
	
	public ItemStackHandler getGroundBlockInventory() {
		return groundBlockInventory;
	}
	
	public ItemStackHandler getRedstoneTorchInventory() {
		return redstoneTorchInventory;
	}
	
	public IItemHandler getFuelInventory() {
		return fuelInventory;
	}
	
	public void readItemStack() {
	}
	
	public void writeItemStack() {
	}
	
	public int getFuel() {
		return fuel;
	}
	
	public void setFuel(int fuel) {
		this.fuel = fuel;
	}
	
	public static class Client extends TrackBuilderInventoryWrapper {
		
		public Client(Supplier<World> worldSupplier) {
			super(worldSupplier);
		}
		
	}
	
	public static class Server extends TrackBuilderInventoryWrapper {
		
		private final ItemStack stack;
		
		public Server(ItemStack stack, Supplier<World> worldSupplier) {
			super(worldSupplier);
			this.stack = stack;
			readItemStack();
		}
		
		@Override
		public void readItemStack() {
			readItemHandler(railInventory, "rail");
			readItemHandler(groundBlockInventory, "ground_block");
			readItemHandler(redstoneTorchInventory, "redstone_torch");
			
			final CompoundNBT compound = stack.getTag();
			if (compound != null) {
				fuel = compound.getInt("fuel");
			}
		}
		
		@Override
		public void writeItemStack() {
			writeItemHandler(railInventory, "rail");
			writeItemHandler(groundBlockInventory, "ground_block");
			writeItemHandler(redstoneTorchInventory, "redstone_torch");
			
			if (fuel > 0) {
				final CompoundNBT compound = stack.getOrCreateTag();
				compound.putInt("fuel", fuel);
			} else {
				final CompoundNBT compound = stack.getTag();
				if (compound != null) {
					compound.remove("fuel");
				}
			}
		}
		
		private void readItemHandler(ItemStackHandler handler, String childCompoundName) {
			final CompoundNBT childCompound = stack.getChildTag(childCompoundName);
			if (childCompound != null) {
				handler.deserializeNBT(childCompound);
			}
		}
		
		private void writeItemHandler(ItemStackHandler handler, String childCompoundName) {
			if (IntStream.range(0, handler.getSlots()).mapToObj(handler::getStackInSlot).allMatch(ItemStack::isEmpty)) {
				stack.removeChildTag(childCompoundName);
			} else {
				stack.getOrCreateTag().put(childCompoundName, handler.serializeNBT());
			}
		}
	}
}
