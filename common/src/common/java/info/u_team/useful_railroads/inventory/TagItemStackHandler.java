package info.u_team.useful_railroads.inventory;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TagItemStackHandler extends FixedSizeItemStackHandler {
	
	private final TagKey<Item> tag;
	
	public TagItemStackHandler(TagKey<Item> tag, int size) {
		super(size);
		this.tag = tag;
	}
	
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return getCondition(stack);
	}
	
	public boolean getCondition(ItemStack stack) {
		return stack.is(tag);
	}
}
