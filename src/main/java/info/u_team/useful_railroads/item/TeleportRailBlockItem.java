package info.u_team.useful_railroads.item;

import info.u_team.useful_railroads.block.TeleportRailBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.*;

public class TeleportRailBlockItem extends BlockItem {
	
	public TeleportRailBlockItem(TeleportRailBlock block, Properties builder) {
		super(block, builder);
	}
	
	@Override
	public ActionResultType tryPlace(BlockItemUseContext context) {
		final CompoundNBT compound = context.getItem().getChildTag("BlockEntityTag");
		if (compound != null && compound.contains("location")) {
			return super.tryPlace(context);
		}
		final PlayerEntity player = context.getPlayer();
		if (player != null) {
			player.sendMessage(new StringTextComponent(TextFormatting.DARK_RED + "You need to setup the rail first."));
		}
		return ActionResultType.FAIL;
	}
	
	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
		return false;
	}
	
}
