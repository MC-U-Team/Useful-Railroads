package info.u_team.useful_railroads.container;

import info.u_team.u_team_core.api.sync.BufferReferenceHolder;
import info.u_team.u_team_core.container.UTileEntityContainer;
import info.u_team.useful_railroads.init.UsefulRailroadsContainerTypes;
import info.u_team.useful_railroads.inventory.FuelItemSlotHandler;
import info.u_team.useful_railroads.tileentity.TeleportRailTileEntity;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class TeleportRailContainer extends UTileEntityContainer<TeleportRailTileEntity> {
	
	// Client
	public TeleportRailContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
		super(UsefulRailroadsContainerTypes.TELEPORT_RAIL.get(), id, playerInventory, buffer);
	}
	
	// Server
	public TeleportRailContainer(int id, PlayerInventory playerInventory, TeleportRailTileEntity tileEntity) {
		super(UsefulRailroadsContainerTypes.TELEPORT_RAIL.get(), id, playerInventory, tileEntity);
	}
	
	@Override
	protected void init(boolean server) {
		appendInventory(tileEntity.getFuelSlot(), FuelItemSlotHandler::new, 1, 1, 152, 75);
		appendPlayerInventory(playerInventory, 8, 107);
		addServerToClientTracker(BufferReferenceHolder.createIntHolder(getTileEntity()::getFuel, getTileEntity()::setFuel));
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int index) {
		ItemStack remainingStack = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack()) {
			final ItemStack stack = slot.getStack();
			remainingStack = stack.copy();
			
			if (index >= 1) {
				if (mergeItemStack(stack, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			}
			
			if (index >= 1 && index <= 27) {
				if (!mergeItemStack(stack, 28, 37, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 28 && index <= 36) {
				if (!mergeItemStack(stack, 1, 28, false)) {
					return ItemStack.EMPTY;
				}
			}
			
			if (stack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		return remainingStack;
	}
}
