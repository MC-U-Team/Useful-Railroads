package info.u_team.useful_railroads.container;

import info.u_team.u_team_core.container.UContainer;
import info.u_team.useful_railroads.init.UsefulRailroadsBlocks;
import info.u_team.useful_railroads.tilentity.TileEntityRailTeleport;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.*;

public class ContainerRailTeleport extends UContainer {
	
	private TileEntityRailTeleport tile;
	public final int[] fields;
	
	public ContainerRailTeleport(TileEntityRailTeleport tile, EntityPlayer player) {
		this.tile = tile;
		this.fields = new int[tile.getFieldCount()];
		addSlotToContainer(new SlotRailTeleportFuel(tile, 0, 152, 32));
		appendPlayerInventory(player.inventory, 8, 64);
	}
	
	@Override
	public void appendPlayerInventory(InventoryPlayer inventory, int x, int y) {
		int startIndex = inventorySlots.size();
		for (int height = 0; height < 4; height++) {
			for (int width = 0; width < 9; width++) {
				if (height == 3) {
					addSlotToContainer(new Slot(inventory, startIndex + width, width * 18 + x, height * 18 + 4 + y));
					continue;
				}
				addSlotToContainer(new Slot(inventory, startIndex + (width + height * 9 + 9), width * 18 + x, height * 18 + y));
			}
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		BlockPos pos = tile.getPos();
		if (player.getEntityWorld().getBlockState(pos).getBlock() != UsefulRailroadsBlocks.rail_teleport) {
			return false;
		} else {
			return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index < 1) {
				if (!this.mergeItemStack(itemstack1, 1, 37, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
				return ItemStack.EMPTY;
			}
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(playerIn, itemstack1);
		}
		return itemstack;
	}
	
	// Sync fields with client
	
	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		if (fields.length > 0) {
			listener.sendAllWindowProperties(this, this.tile);
		}
	}
	
	@Override
	public void detectAndSendChanges() {
		for (int i = 0; i < this.inventorySlots.size(); ++i) {
			ItemStack itemstack = ((Slot) this.inventorySlots.get(i)).getStack();
			ItemStack itemstack1 = this.inventoryItemStacks.get(i);
			
			if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
				boolean clientStackChanged = !ItemStack.areItemStacksEqualUsingNBTShareTag(itemstack1, itemstack);
				itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();
				this.inventoryItemStacks.set(i, itemstack1);
				
				if (clientStackChanged) {
					for (int j = 0; j < this.listeners.size(); ++j) {
						((IContainerListener) this.listeners.get(j)).sendSlotContents(this, i, itemstack1);
					}
				}
			}
		}
		for (int j = 0; j < fields.length; j++) {
			for (int i = 0; i < this.listeners.size(); ++i) {
				IContainerListener icontainerlistener = this.listeners.get(i);
				if (this.fields[j] != this.tile.getField(j)) {
					icontainerlistener.sendWindowProperty(this, j, this.tile.getField(j));
				}
			}
			this.fields[j] = this.tile.getField(0);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		tile.setField(id, data);
	}
	
}
