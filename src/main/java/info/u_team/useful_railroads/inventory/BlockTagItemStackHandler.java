package info.u_team.useful_railroads.inventory;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BlockTagItemStackHandler extends TagItemStackHandler {
	
	public BlockTagItemStackHandler(TagKey<Item> tag, int size) {
		super(tag, size);
	}
	
	@Override
	public boolean getCondition(ItemStack stack) {
		return super.getCondition(stack) && stack.getItem() instanceof BlockItem;
	}
}
