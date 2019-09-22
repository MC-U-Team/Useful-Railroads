package info.u_team.useful_railroads.inventory;

import net.minecraft.item.*;
import net.minecraft.tags.Tag;
import net.minecraftforge.items.ItemStackHandler;

public class TagItemStackHandler extends ItemStackHandler {
	
	private final Tag<Item> tag;
	
	public TagItemStackHandler(Tag<Item> tag, int size) {
		super(size);
		this.tag = tag;
	}
	
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return stack.getItem().isIn(tag);
	}
}
