package info.u_team.useful_railroads.inventory;

import info.u_team.u_team_core.inventory.UItemStackContainer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TagItemContainer extends UItemStackContainer {
	
	private final TagKey<Item> tag;
	
	public TagItemContainer(TagKey<Item> tag, int size) {
		super(size);
		this.tag = tag;
	}
	
	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return getCondition(stack);
	}
	
	public boolean getCondition(ItemStack stack) {
		return stack.is(tag);
	}
}
