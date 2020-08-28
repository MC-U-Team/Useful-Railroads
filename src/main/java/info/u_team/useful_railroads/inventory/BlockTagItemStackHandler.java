package info.u_team.useful_railroads.inventory;

import net.minecraft.item.*;
import net.minecraft.tags.ITag;

public class BlockTagItemStackHandler extends TagItemStackHandler {
	
	public BlockTagItemStackHandler(ITag<Item> tag, int size) {
		super(tag, size);
	}
	
	@Override
	public boolean getCondition(ItemStack stack) {
		return super.getCondition(stack) && stack.getItem() instanceof BlockItem;
	}
}
