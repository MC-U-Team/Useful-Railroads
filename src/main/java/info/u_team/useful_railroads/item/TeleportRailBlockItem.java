package info.u_team.useful_railroads.item;

import info.u_team.useful_railroads.block.TeleportRailBlock;
import info.u_team.useful_railroads.util.Location;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

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
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity itemEntity) {
		final CompoundNBT compound = stack.getChildTag("BlockEntityTag");
		if (compound != null && compound.contains("location")) { // Prevent overwriting already installed rails
			return false;
		}
		final World world = itemEntity.getEntityWorld();
		if (world.isRemote) {
			// Do client things
		} else {
			final int age = itemEntity.age;
			if (age < 100) {
				return false;
			}
			final AxisAlignedBB aabb = new AxisAlignedBB(itemEntity.posX - 1, itemEntity.posY - 1, itemEntity.posZ - 1, itemEntity.posX + 1, itemEntity.posY + 1, itemEntity.posZ + 1);
			world.getEntitiesInAABBexcluding(itemEntity, aabb, ItemEntity.class::isInstance).stream() //
					.map(ItemEntity.class::cast) //
					.map(ItemEntity::getItem) //
					.filter(otherStack -> otherStack.getItem() == Items.ENDER_PEARL) //
					.findAny() //
					.ifPresent(otherStack -> {
						stack.shrink(1);
						compound.put("location", new Location(world.getDimension().getType(), itemEntity.getPosition()).serializeNBT());
						// itemEntity.setItem(stack);
						world.addEntity(new LightningBoltEntity(world, itemEntity.posX, itemEntity.posY, itemEntity.posZ, true));
					});
		}
		return false;
	}
}
