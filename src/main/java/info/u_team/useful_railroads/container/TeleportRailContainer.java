package info.u_team.useful_railroads.container;

import info.u_team.u_team_core.api.sync.DataHolder;
import info.u_team.u_team_core.menu.UBlockEntityContainerMenu;
import info.u_team.useful_railroads.init.UsefulRailroadsContainerTypes;
import info.u_team.useful_railroads.inventory.FuelItemSlotHandler;
import info.u_team.useful_railroads.tileentity.TeleportRailTileEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;

public class TeleportRailContainer extends UBlockEntityContainerMenu<TeleportRailTileEntity> {
	
	// Client
	public TeleportRailContainer(int containerId, Inventory playerInventory, FriendlyByteBuf buffer) {
		super(UsefulRailroadsContainerTypes.TELEPORT_RAIL.get(), containerId, playerInventory, buffer);
	}
	
	// Server
	public TeleportRailContainer(int containerId, Inventory playerInventory, TeleportRailTileEntity blockEntity) {
		super(UsefulRailroadsContainerTypes.TELEPORT_RAIL.get(), containerId, playerInventory, blockEntity);
	}
	
	@Override
	protected void init(LogicalSide side) {
		addSlots(blockEntity.getFuelSlot(), FuelItemSlotHandler::new, 1, 1, 152, 75);
		addPlayerInventory(playerInventory, 8, 107);
		addDataHolderToClient(DataHolder.createIntHolder(getBlockEntity()::getFuel, getBlockEntity()::setFuel));
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack remainingStack = ItemStack.EMPTY;
		final Slot slot = slots.get(index);
		
		if (slot != null && slot.hasItem()) {
			final ItemStack stack = slot.getItem();
			remainingStack = stack.copy();
			
			if (index >= 1) {
				if (moveItemStackTo(stack, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			}
			
			if (index >= 1 && index <= 27) {
				if (!moveItemStackTo(stack, 28, 37, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 28 && index <= 36) {
				if (!moveItemStackTo(stack, 1, 28, false)) {
					return ItemStack.EMPTY;
				}
			}
			
			if (stack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		return remainingStack;
	}
}
