package info.u_team.useful_railroads.inventory;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;

public class TagItemStackHandler extends FixedSizeItemStackHandler {
	
	private final ITag<Item> tag;
	
	public TagItemStackHandler(ITag<Item> tag, int size) {
		super(size);
		this.tag = tag;
	}
	
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return getCondition(stack);
	}
	
	public boolean getCondition(ItemStack stack) {
		return stack.getItem().isIn(tag);
	}
}
