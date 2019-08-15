package info.u_team.useful_railroads.tileentity;

import java.util.Map.Entry;
import java.util.function.Supplier;

import info.u_team.useful_railroads.config.CommonExtendedConfig;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class TeleportRailItemHandler implements IItemHandlerModifiable {
	
	private final Supplier<Boolean> isRemote;
	private final Consumer<Integer> fuelAdder;
	
	public TeleportRailItemHandler(Supplier<Boolean> isRemote, Consumer<Integer> fuelAdder) {
		this.isRemote = isRemote;
		this.fuelAdder = fuelAdder;
	}
	
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return CommonExtendedConfig.getFuel().keySet().stream().filter(ingedient -> ingedient.test(stack)).findAny().isPresent();
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		
		if (!isItemValid(slot, stack)) {
			return stack;
		}
		if (!simulate) {
			setStackInSlot(slot, stack);
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public int getSlots() {
		return 1;
	}
	
	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		if (!isRemote.get()) {
			CommonExtendedConfig.getFuel().entrySet().stream().filter(entry -> entry.getKey().test(stack)).findAny().map(Entry::getValue).ifPresent(value -> {
				fuelAdder.accept(stack.getCount() * value);
			});
		}
	}
}
