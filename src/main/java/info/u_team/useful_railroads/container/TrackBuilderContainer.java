package info.u_team.useful_railroads.container;

import info.u_team.u_team_core.container.UContainer;
import info.u_team.useful_railroads.init.UsefulRailroadsContainerTypes;
import info.u_team.useful_railroads.inventory.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IntReferenceHolder;

public class TrackBuilderContainer extends UContainer {
	
	private final TrackBuilderInventoryWrapper wrapper;
	
	// Client
	public TrackBuilderContainer(int id, PlayerInventory playerInventory) {
		this(id, playerInventory, new TrackBuilderInventoryWrapper.Client(() -> playerInventory.player.world));
	}
	
	// Server
	public TrackBuilderContainer(int id, PlayerInventory playerInventory, TrackBuilderInventoryWrapper wrapper) {
		super(UsefulRailroadsContainerTypes.TRACK_BUILDER, id);
		this.wrapper = wrapper;
		appendInventory(wrapper.getRailInventory(), 1, 9, 8, 32);
		appendInventory(wrapper.getGroundBlockInventory(), 3, 9, 8, 64);
		appendInventory(wrapper.getRedstoneTorchInventory(), 1, 2, 8, 132);
		appendInventory(wrapper.getFuelInventory(), FuelItemSlotHandler::new, 1, 1, 152, 132);
		appendPlayerInventory(playerInventory, 8, 164);
		trackInt(new IntReferenceHolder() {
			
			@Override
			public int get() {
				return wrapper.getFuel();
			}
			
			@Override
			public void set(int fuel) {
				wrapper.setFuel(fuel);
			}
			
		});
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		wrapper.writeItemStack();
	}
	
	public TrackBuilderInventoryWrapper getWrapper() {
		return wrapper;
	}
	
}
