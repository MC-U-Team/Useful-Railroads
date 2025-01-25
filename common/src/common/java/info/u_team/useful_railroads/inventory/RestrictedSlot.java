package info.u_team.useful_railroads.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class RestrictedSlot extends Slot {
	
	public RestrictedSlot(Container container, int index, int x, int y) {
		super(container, index, x, y);
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return container.canPlaceItem(index, stack);
	}
	
}
