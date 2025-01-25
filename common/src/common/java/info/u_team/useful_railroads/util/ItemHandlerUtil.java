package info.u_team.useful_railroads.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class ItemHandlerUtil {
	
	public static int getItemCount(Container container, Predicate<? super ItemStack> predicate) {
		return getStackStream(container).filter(predicate).mapToInt(ItemStack::getCount).sum();
	}
	
	public static Stream<ItemStack> getStackStream(Container container) {
		return IntStream.range(0, container.getContainerSize()).mapToObj(container::getItem);
	}
	
	public static List<ItemStack> extractItems(Container container, Predicate<? super ItemStack> predicate, int extractCount) {
		final List<ItemStack> list = new ArrayList<>();
		
		final AtomicInteger countLeft = new AtomicInteger(extractCount);
		IntStream.range(0, container.getContainerSize()).filter(slot -> predicate.test(container.getItem(slot))).forEach(slot -> {
			final int count = countLeft.get();
			if (count > 0) {
				final ItemStack stack = container.removeItem(slot, extractCount);
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
		final ItemStack oneItemStack = stack.copyWithCount(1);
		stack.shrink(1);
		if (stack.isEmpty()) {
			list.remove(0);
		}
		return oneItemStack;
	}
	
}
