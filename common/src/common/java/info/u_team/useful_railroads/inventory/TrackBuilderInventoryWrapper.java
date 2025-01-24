package info.u_team.useful_railroads.inventory;

import java.util.function.Supplier;

import info.u_team.useful_railroads.component.TrackBuilderComponent;
import info.u_team.useful_railroads.init.UsefulRailroadsDataComponentTypes;
import info.u_team.useful_railroads.init.UsefulRailroadsRecipeTypes;
import info.u_team.useful_railroads.init.UsefulRailroadsTags;
import info.u_team.useful_railroads.util.TrackBuilderMode;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

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
		fuelInventory = new FuelItemContainer<>(UsefulRailroadsRecipeTypes.TRACK_BUILDER_FUEL.get(), levelSupplier, fuelAdder -> fuel += fuelAdder);
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
			final TrackBuilderComponent component = stack.get(UsefulRailroadsDataComponentTypes.TRACK_BUILDER.get());
			if (component != null) {
				component.getRails().copyInto(railInventory.getItems());
				component.getGroundBlocks().copyInto(groundInventory.getItems());
				component.getTunnelBlocks().copyInto(tunnelInventory.getItems());
				component.getRedstoneTorches().copyInto(redstoneTorchInventory.getItems());
				component.getTorches().copyInto(torchInventory.getItems());
				fuel = component.getFuel();
				mode = component.getMode();
			}
		}
		
		@Override
		public void writeItemStack() {
			final TrackBuilderComponent component = TrackBuilderComponent.of(railInventory.getItems(), groundInventory.getItems(), tunnelInventory.getItems(), redstoneTorchInventory.getItems(), torchInventory.getItems(), fuel, mode);
			stack.set(UsefulRailroadsDataComponentTypes.TRACK_BUILDER.get(), component);
		}
		
		public ItemStack getStack() {
			return stack;
		}
	}
}
