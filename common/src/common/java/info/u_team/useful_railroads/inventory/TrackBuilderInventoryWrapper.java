package info.u_team.useful_railroads.inventory;

import java.util.function.Supplier;

import info.u_team.useful_railroads.component.TrackBuilderComponent;
import info.u_team.useful_railroads.init.UsefulRailroadsDataComponentTypes;
import info.u_team.useful_railroads.init.UsefulRailroadsRecipeTypes;
import info.u_team.useful_railroads.init.UsefulRailroadsTags;
import info.u_team.useful_railroads.recipe.TrackBuilderFuelRecipe;
import info.u_team.useful_railroads.util.TrackBuilderMode;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TrackBuilderInventoryWrapper {
	
	protected final BlockTagItemContainer railInventory = new BlockTagItemContainer(UsefulRailroadsTags.Items.TRACK_BUILDER_RAILS, 15);
	protected final BlockTagItemContainer groundInventory = new BlockTagItemContainer(UsefulRailroadsTags.Items.TRACK_BUILDER_GROUND_BLOCKS, 30);
	protected final BlockTagItemContainer tunnelInventory = new BlockTagItemContainer(UsefulRailroadsTags.Items.TRACK_BUILDER_TUNNEL_BLOCKS, 45);
	protected final BlockTagItemContainer redstoneTorchInventory = new BlockTagItemContainer(UsefulRailroadsTags.Items.TRACK_BUILDER_REDSTONE_TORCHES, 5);
	protected final BlockTagItemContainer torchInventory = new BlockTagItemContainer(UsefulRailroadsTags.Items.TRACK_BUILDER_TORCHES, 4);
	protected final FuelItemContainer<TrackBuilderFuelRecipe> fuelInventory;
	
	protected int fuel = 0;
	
	protected TrackBuilderMode mode = TrackBuilderMode.MODE_NOAIR;
	
	private TrackBuilderInventoryWrapper(Supplier<Level> levelSupplier) {
		fuelInventory = new FuelItemContainer<>(UsefulRailroadsRecipeTypes.TRACK_BUILDER_FUEL.get(), levelSupplier, fuelAdder -> fuel += fuelAdder);
	}
	
	public BlockTagItemContainer getRailInventory() {
		return railInventory;
	}
	
	public BlockTagItemContainer getGroundInventory() {
		return groundInventory;
	}
	
	public BlockTagItemContainer getTunnelInventory() {
		return tunnelInventory;
	}
	
	public BlockTagItemContainer getRedstoneTorchInventory() {
		return redstoneTorchInventory;
	}
	
	public BlockTagItemContainer getTorchInventory() {
		return torchInventory;
	}
	
	public Container getFuelInventory() {
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
