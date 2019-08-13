package info.u_team.useful_railroads.container;

import info.u_team.u_team_core.container.UTileeEntityContainer;
import info.u_team.useful_railroads.init.UsefulRailroadsContainers;
import info.u_team.useful_railroads.tileentity.TeleportRailTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class TeleportRailContainer extends UTileeEntityContainer<TeleportRailTileEntity> {
	
	public TeleportRailContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
		super(UsefulRailroadsContainers.TELEPORT_RAIL, id, playerInventory, buffer);
	}
	
	public TeleportRailContainer(int id, PlayerInventory playerInventory, TeleportRailTileEntity tileEntity) {
		super(UsefulRailroadsContainers.TELEPORT_RAIL, id, playerInventory, tileEntity);
	}
	
	@Override
	protected void init(boolean server) {
		
	}
	
}
