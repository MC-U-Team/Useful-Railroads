package info.u_team.useful_railroads.item;

import info.u_team.useful_railroads.block.TeleportRailBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.*;

public class TeleportRailBlockItem extends BlockItem {
	
	public TeleportRailBlockItem(TeleportRailBlock block, Properties builder) {
		super(block, builder);
	}
	
	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
		// stack.getChildTag("BlockEntityTag")
		return false;
	}
	
}
