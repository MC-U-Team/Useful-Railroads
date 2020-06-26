package info.u_team.useful_railroads.container;

import info.u_team.u_team_core.api.sync.*;
import info.u_team.u_team_core.api.sync.MessageHolder.EmptyMessageHolder;
import info.u_team.u_team_core.container.UContainer;
import info.u_team.useful_railroads.init.UsefulRailroadsContainerTypes;
import info.u_team.useful_railroads.inventory.*;
import info.u_team.useful_railroads.util.TrackBuilderMode;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class TrackBuilderContainer extends UContainer {
	
	private final TrackBuilderInventoryWrapper wrapper;
	
	private final EmptyMessageHolder changeModeMessage;
	
	// Client
	public TrackBuilderContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
		this(id, playerInventory, new TrackBuilderInventoryWrapper.Client(buffer.readVarInt(), buffer.readEnumValue(TrackBuilderMode.class), () -> playerInventory.player.world));
	}
	
	// Server
	public TrackBuilderContainer(int id, PlayerInventory playerInventory, TrackBuilderInventoryWrapper wrapper) {
		super(UsefulRailroadsContainerTypes.TRACK_BUILDER.get(), id);
		this.wrapper = wrapper;
		appendInventory(wrapper.getFuelInventory(), FuelItemSlotHandler::new, 1, 1, 260, 182);
		appendInventory(wrapper.getRailInventory(), 1, 15, 8, 32);
		appendInventory(wrapper.getGroundInventory(), 2, 15, 8, 64);
		appendInventory(wrapper.getTunnelInventory(), 3, 15, 8, 114);
		appendInventory(wrapper.getRedstoneTorchInventory(), 1, 5, 8, 182);
		appendInventory(wrapper.getTorchInventory(), 1, 4, 116, 182);
		appendPlayerInventory(playerInventory, 62, 214);
		addServerToClientTracker(BufferReferenceHolder.createIntHolder(wrapper::getFuel, wrapper::setFuel));
		addServerToClientTracker(BufferReferenceHolder.createByteHolder(() -> (byte) wrapper.getMode().ordinal(), value -> wrapper.setMode(TrackBuilderMode.class.getEnumConstants()[value])));
		changeModeMessage = addClientToServerTracker(new EmptyMessageHolder(() -> {
			wrapper.setMode(TrackBuilderMode.cycle(wrapper.getMode()));
			wrapper.writeItemStack();
		}));
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		wrapper.writeItemStack();
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int index) {
		ItemStack remainingStack = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack()) {
			final ItemStack stack = slot.getStack();
			remainingStack = stack.copy();
			
			if (index < 97) {
				if (!mergeItemStack(stack, 97, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (index >= 124) {
					if (!mergeItemStack(stack, 0, 124, false)) {
						return ItemStack.EMPTY;
					}
				} else {
					if (!mergeItemStack(stack, 0, 97, false)) {
						if (!mergeItemStack(stack, 124, 133, false)) {
							return ItemStack.EMPTY;
						}
					}
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
	
	@Override
	public ItemStack slotClick(int index, int dragType, ClickType clickType, PlayerEntity player) {
		Slot tmpSlot;
		if (index >= 0 && index < inventorySlots.size()) {
			tmpSlot = inventorySlots.get(index);
		} else {
			tmpSlot = null;
		}
		if (tmpSlot != null) {
			if (tmpSlot.inventory == player.inventory && tmpSlot.getSlotIndex() == player.inventory.currentItem) {
				return tmpSlot.getStack();
			}
		}
		if (clickType == ClickType.SWAP) {
			ItemStack stack = player.inventory.getStackInSlot(dragType);
			if (stack == player.inventory.getCurrentItem()) {
				return ItemStack.EMPTY;
			}
		}
		return super.slotClick(index, dragType, clickType, player);
	}
	
	public TrackBuilderInventoryWrapper getWrapper() {
		return wrapper;
	}
	
	public EmptyMessageHolder getChangeModeMessage() {
		return changeModeMessage;
	}
	
}
