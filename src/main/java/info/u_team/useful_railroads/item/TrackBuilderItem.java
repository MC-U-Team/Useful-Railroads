package info.u_team.useful_railroads.item;

import info.u_team.u_team_core.item.UItem;
import info.u_team.useful_railroads.container.TrackBuilderContainer;
import info.u_team.useful_railroads.init.UsefulRailroadsItemGroups;
import info.u_team.useful_railroads.inventory.TrackBuilderInventoryWrapper;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class TrackBuilderItem extends UItem {
	
	public TrackBuilderItem(String name) {
		super(name, UsefulRailroadsItemGroups.GROUP, new Properties().maxStackSize(1).rarity(Rarity.RARE));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		final ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote) {
			if (!player.isSneaking()) {
				openGui(stack, player);
			}
			
		}
		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}
	
	private void openGui(ItemStack stack, PlayerEntity player) {
		if (!(player instanceof ServerPlayerEntity)) {
			return;
		}
		final TrackBuilderInventoryWrapper wrapper = new TrackBuilderInventoryWrapper.Server(stack, () -> player.world);
		NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
			
			@Override
			public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
				return new TrackBuilderContainer(id, playerInventory, wrapper);
			}
			
			@Override
			public ITextComponent getDisplayName() {
				return stack.getDisplayName();
			}
		}, buffer -> buffer.writeVarInt(wrapper.getFuel()));
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return !ItemStack.areItemsEqual(oldStack, newStack);
	}
}
