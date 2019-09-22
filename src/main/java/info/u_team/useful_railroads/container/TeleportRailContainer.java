package info.u_team.useful_railroads.container;

import info.u_team.u_team_core.container.UTileEntityContainer;
import info.u_team.useful_railroads.init.UsefulRailroadsContainerTypes;
import info.u_team.useful_railroads.inventory.FuelItemSlotHandler;
import info.u_team.useful_railroads.tileentity.TeleportRailTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.items.CapabilityItemHandler;

public class TeleportRailContainer extends UTileEntityContainer<TeleportRailTileEntity> {
	
	// Client
	public TeleportRailContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
		super(UsefulRailroadsContainerTypes.TELEPORT_RAIL, id, playerInventory, buffer);
	}
	
	// Server
	public TeleportRailContainer(int id, PlayerInventory playerInventory, TeleportRailTileEntity tileEntity) {
		super(UsefulRailroadsContainerTypes.TELEPORT_RAIL, id, playerInventory, tileEntity);
	}
	
	@Override
	protected void init(boolean server) {
		tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> appendInventory(handler, FuelItemSlotHandler::new, 1, 1, 152, 75));
		appendPlayerInventory(playerInventory, 8, 107);
		trackInt(new IntReferenceHolder() {
			
			@Override
			public int get() {
				return getTileEntity().getFuel();
			}
			
			@Override
			public void set(int fuel) {
				getTileEntity().setFuel(fuel);
			}
			
		});
	}
}
