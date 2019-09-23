package info.u_team.useful_railroads.util;

import java.util.function.Predicate;
import java.util.stream.*;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemHandlerUtil {
	
	public static int getItemCount(IItemHandler handler, Predicate<? super ItemStack> predicate) {
		return getStackStream(handler).filter(predicate).mapToInt(ItemStack::getCount).sum();
	}
	
	public static Stream<ItemStack> getStackStream(IItemHandler handler) {
		return IntStream.range(0, handler.getSlots()).mapToObj(handler::getStackInSlot);
	}
	
}
