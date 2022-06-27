package info.u_team.useful_railroads.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemHandlerUtil {
	
	public static int getItemCount(IItemHandler handler, Predicate<? super ItemStack> predicate) {
		return getStackStream(handler).filter(predicate).mapToInt(ItemStack::getCount).sum();
	}
	
	public static Stream<ItemStack> getStackStream(IItemHandler handler) {
		return IntStream.range(0, handler.getSlots()).mapToObj(handler::getStackInSlot);
	}
	
	public static List<ItemStack> extractItems(IItemHandler handler, Predicate<? super ItemStack> predicate, int extractCount) {
		final List<ItemStack> list = new ArrayList<>();
		
		final AtomicInteger countLeft = new AtomicInteger(extractCount);
		IntStream.range(0, handler.getSlots()).filter(slot -> predicate.test(handler.getStackInSlot(slot))).forEach(slot -> {
			final int count = countLeft.get();
			if (count > 0) {
				final ItemStack stack = handler.extractItem(slot, count, false);
				countLeft.set(count - stack.getCount());
				list.add(stack);
			}
		});
		return list;
	}
	
	public static ItemStack getOneItemAndRemove(List<ItemStack> list) {
		if (list.isEmpty()) {
			return ItemStack.EMPTY;
		}
		final ItemStack stack = list.get(0);
		final ItemStack oneItemStack = ItemHandlerHelper.copyStackWithSize(stack, 1);
		stack.shrink(1);
		if (stack.isEmpty()) {
			list.remove(0);
		}
		return oneItemStack;
	}
	
}
