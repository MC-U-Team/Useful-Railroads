package info.u_team.useful_railroads.inventory;

import net.minecraft.item.*;
import net.minecraft.tags.Tag;

public class BlockTagItemStackHandler extends TagItemStackHandler {
	
	public BlockTagItemStackHandler(Tag<Item> tag, int size) {
		super(tag, size);
	}
	
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return super.isItemValid(slot, stack) && stack.getItem() instanceof BlockItem;
	}
	
}
