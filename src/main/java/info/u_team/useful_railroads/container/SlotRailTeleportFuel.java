package info.u_team.useful_railroads.container;

import info.u_team.useful_railroads.tilentity.TileEntityRailTeleport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotRailTeleportFuel extends Slot {
	
	private TileEntityRailTeleport tile;
	
	public SlotRailTeleportFuel(TileEntityRailTeleport tile, int index, int x, int y) {
		super(tile, index, x, y);
		this.tile = tile;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return tile.isItemValidForSlot(getSlotIndex(), stack);
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		return false;
	}
	
}
