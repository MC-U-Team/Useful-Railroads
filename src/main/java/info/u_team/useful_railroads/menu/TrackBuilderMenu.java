package info.u_team.useful_railroads.menu;

import info.u_team.u_team_core.api.sync.DataHolder;
import info.u_team.u_team_core.api.sync.MessageHolder.EmptyMessageHolder;
import info.u_team.u_team_core.menu.ItemHandlerSlotCreator;
import info.u_team.u_team_core.menu.UContainerMenu;
import info.u_team.useful_railroads.init.UsefulRailroadsMenuTypes;
import info.u_team.useful_railroads.inventory.FuelItemSlotHandler;
import info.u_team.useful_railroads.inventory.TrackBuilderInventoryWrapper;
import info.u_team.useful_railroads.item.TrackBuilderItem;
import info.u_team.useful_railroads.util.TrackBuilderMode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TrackBuilderMenu extends UContainerMenu {
	
	private final TrackBuilderInventoryWrapper wrapper;
	
	private final EmptyMessageHolder changeModeMessage;
	
	private final int selectedSlot;
	
	// Client
	public TrackBuilderMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buffer) {
		this(containerId, playerInventory, new TrackBuilderInventoryWrapper.Client(buffer.readVarInt(), buffer.readEnum(TrackBuilderMode.class), playerInventory.player::level), buffer.readVarInt());
	}
	
	// Server
	public TrackBuilderMenu(int containerId, Inventory playerInventory, TrackBuilderInventoryWrapper wrapper, int selectedSlot) {
		super(UsefulRailroadsMenuTypes.TRACK_BUILDER.get(), containerId);
		this.wrapper = wrapper;
		this.selectedSlot = selectedSlot;
		addSlots((index, xPosition, yPosition) -> new FuelItemSlotHandler(wrapper.getFuelInventory(), index, xPosition, yPosition), 1, 1, 260, 182);
		addSlots(ItemHandlerSlotCreator.of(wrapper.getRailInventory()), 1, 15, 8, 32);
		addSlots(ItemHandlerSlotCreator.of(wrapper.getGroundInventory()), 2, 15, 8, 64);
		addSlots(ItemHandlerSlotCreator.of(wrapper.getTunnelInventory()), 3, 15, 8, 114);
		addSlots(ItemHandlerSlotCreator.of(wrapper.getRedstoneTorchInventory()), 1, 5, 8, 182);
		addSlots(ItemHandlerSlotCreator.of(wrapper.getTorchInventory()), 1, 4, 116, 182);
		addPlayerInventory(playerInventory, 62, 214);
		addDataHolderToClient(DataHolder.createIntHolder(wrapper::getFuel, wrapper::setFuel));
		addDataHolderToClient(DataHolder.createByteHolder(() -> (byte) wrapper.getMode().ordinal(), value -> wrapper.setMode(TrackBuilderMode.class.getEnumConstants()[value])));
		changeModeMessage = addDataHolderToServer(new EmptyMessageHolder(() -> {
			wrapper.setMode(TrackBuilderMode.cycle(wrapper.getMode()));
			wrapper.writeItemStack();
		}));
	}
	
	@Override
	public void broadcastChanges() {
		super.broadcastChanges();
		wrapper.writeItemStack();
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack remainingStack = ItemStack.EMPTY;
		final Slot slot = slots.get(index);
		
		if (slot != null && slot.hasItem()) {
			final ItemStack stack = slot.getItem();
			remainingStack = stack.copy();
			
			if (index < 97) {
				if (!moveItemStackTo(stack, 97, slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (index >= 124) {
					if (!moveItemStackTo(stack, 0, 124, false)) {
						return ItemStack.EMPTY;
					}
				} else {
					if (!moveItemStackTo(stack, 0, 97, false)) {
						if (!moveItemStackTo(stack, 124, 133, false)) {
							return ItemStack.EMPTY;
						}
					}
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
	
	@Override
	public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
		Slot tmpSlot;
		if (slotId >= 0 && slotId < slots.size()) {
			tmpSlot = slots.get(slotId);
		} else {
			tmpSlot = null;
		}
		if (tmpSlot != null) {
			if (tmpSlot.container == player.getInventory() && tmpSlot.getSlotIndex() == selectedSlot) {
				// return tmpSlot.getItem(); // TODO just return??
				return;
			}
		}
		if (clickType == ClickType.SWAP) {
			final ItemStack stack = player.getInventory().getItem(dragType);
			final ItemStack currentItem = Inventory.isHotbarSlot(selectedSlot) ? player.getInventory().items.get(selectedSlot) : selectedSlot == -1 ? player.getInventory().offhand.get(0) : ItemStack.EMPTY;
			
			if (!currentItem.isEmpty() && stack == currentItem) {
				// return ItemStack.EMPTY; // TODO just return??
				return;
			}
		}
		super.clicked(slotId, dragType, clickType, player);
	}
	
	@Override
	public boolean stillValid(Player player) {
		if (wrapper instanceof TrackBuilderInventoryWrapper.Server) {
			final ItemStack stack = ((TrackBuilderInventoryWrapper.Server) wrapper).getStack();
			return !stack.isEmpty() && stack.getItem() instanceof TrackBuilderItem;
		}
		return true;
	}
	
	public TrackBuilderInventoryWrapper getWrapper() {
		return wrapper;
	}
	
	public EmptyMessageHolder getChangeModeMessage() {
		return changeModeMessage;
	}
	
}
