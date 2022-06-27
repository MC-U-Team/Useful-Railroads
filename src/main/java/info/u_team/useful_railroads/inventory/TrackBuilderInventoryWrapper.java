package info.u_team.useful_railroads.inventory;

import java.util.function.Supplier;
import java.util.stream.IntStream;

import info.u_team.useful_railroads.init.UsefulRailroadsRecipeTypes;
import info.u_team.useful_railroads.init.UsefulRailroadsTags;
import info.u_team.useful_railroads.util.TrackBuilderMode;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TrackBuilderInventoryWrapper {
	
	protected final BlockTagItemStackHandler railInventory = new BlockTagItemStackHandler(UsefulRailroadsTags.Items.TRACK_BUILDER_RAILS, 15);
	protected final BlockTagItemStackHandler groundInventory = new BlockTagItemStackHandler(UsefulRailroadsTags.Items.TRACK_BUILDER_GROUND_BLOCKS, 30);
	protected final BlockTagItemStackHandler tunnelInventory = new BlockTagItemStackHandler(UsefulRailroadsTags.Items.TRACK_BUILDER_TUNNEL_BLOCKS, 45);
	protected final BlockTagItemStackHandler redstoneTorchInventory = new BlockTagItemStackHandler(UsefulRailroadsTags.Items.TRACK_BUILDER_REDSTONE_TORCHES, 5);
	protected final BlockTagItemStackHandler torchInventory = new BlockTagItemStackHandler(UsefulRailroadsTags.Items.TRACK_BUILDER_TORCHES, 4);
	protected final IItemHandler fuelInventory;
	
	protected int fuel = 0;
	
	protected TrackBuilderMode mode = TrackBuilderMode.MODE_NOAIR;
	
	private TrackBuilderInventoryWrapper(Supplier<Level> levelSupplier) {
		fuelInventory = new FuelItemHandler<>(UsefulRailroadsRecipeTypes.TRACK_BUILDER_FUEL.get(), levelSupplier, fuelAdder -> fuel += fuelAdder);
	}
	
	public BlockTagItemStackHandler getRailInventory() {
		return railInventory;
	}
	
	public BlockTagItemStackHandler getGroundInventory() {
		return groundInventory;
	}
	
	public BlockTagItemStackHandler getTunnelInventory() {
		return tunnelInventory;
	}
	
	public BlockTagItemStackHandler getRedstoneTorchInventory() {
		return redstoneTorchInventory;
	}
	
	public BlockTagItemStackHandler getTorchInventory() {
		return torchInventory;
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
	
	public TrackBuilderMode getMode() {
		return mode;
	}
	
	public void setMode(TrackBuilderMode mode) {
		this.mode = mode;
	}
	
	public static class Client extends TrackBuilderInventoryWrapper {
		
		public Client(int fuel, TrackBuilderMode mode, Supplier<Level> levelSupplier) {
			super(levelSupplier);
			this.fuel = fuel;
			this.mode = mode;
		}
		
	}
	
	public static class Server extends TrackBuilderInventoryWrapper {
		
		private final ItemStack stack;
		
		public Server(ItemStack stack, Supplier<Level> levelSupplier) {
			super(levelSupplier);
			this.stack = stack;
			readItemStack();
		}
		
		@Override
		public void readItemStack() {
			readItemHandler(railInventory, "rail");
			readItemHandler(groundInventory, "ground");
			readItemHandler(tunnelInventory, "tunnel");
			readItemHandler(redstoneTorchInventory, "redstone_torch");
			readItemHandler(torchInventory, "torch");
			
			final CompoundTag compound = stack.getTag();
			if (compound != null) {
				fuel = compound.getInt("fuel");
				mode = TrackBuilderMode.byName(compound.getString("mode"));
			}
		}
		
		@Override
		public void writeItemStack() {
			writeItemHandler(railInventory, "rail");
			writeItemHandler(groundInventory, "ground");
			writeItemHandler(tunnelInventory, "tunnel");
			writeItemHandler(redstoneTorchInventory, "redstone_torch");
			writeItemHandler(torchInventory, "torch");
			
			if (fuel > 0) {
				final CompoundTag compound = stack.getOrCreateTag();
				compound.putInt("fuel", fuel);
			} else {
				final CompoundTag compound = stack.getTag();
				if (compound != null) {
					compound.remove("fuel");
				}
			}
			
			if (mode != TrackBuilderMode.MODE_NOAIR) {
				final CompoundTag compound = stack.getOrCreateTag();
				compound.putString("mode", mode.getName());
			} else {
				final CompoundTag compound = stack.getTag();
				if (compound != null) {
					compound.remove("mode");
				}
			}
		}
		
		private void readItemHandler(ItemStackHandler handler, String childCompoundName) {
			final CompoundTag childCompound = stack.getTagElement(childCompoundName);
			if (childCompound != null) {
				handler.deserializeNBT(childCompound);
			}
		}
		
		private void writeItemHandler(ItemStackHandler handler, String childCompoundName) {
			if (IntStream.range(0, handler.getSlots()).mapToObj(handler::getStackInSlot).allMatch(ItemStack::isEmpty)) {
				stack.removeTagKey(childCompoundName);
			} else {
				stack.getOrCreateTag().put(childCompoundName, handler.serializeNBT());
			}
		}
		
		public ItemStack getStack() {
			return stack;
		}
	}
}
