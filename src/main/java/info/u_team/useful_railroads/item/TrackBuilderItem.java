package info.u_team.useful_railroads.item;

import info.u_team.u_team_core.item.UItem;
import info.u_team.useful_railroads.container.TrackBuilderContainer;
import info.u_team.useful_railroads.init.UsefulRailroadsItemGroups;
import info.u_team.useful_railroads.inventory.TrackBuilderInventoryWrapper;
import info.u_team.useful_railroads.util.TrackBuilderManager;
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
		if (!world.isRemote && !player.isSneaking() && player instanceof ServerPlayerEntity) { // Server & Player not sneaking
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
		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		final World world = context.getWorld();
		if (world.isRemote) { // Server side
			return ActionResultType.PASS;
		}
		final PlayerEntity player = context.getPlayer();
		if (player == null || !context.isPlacerSneaking()) { // No player or no sneaking
			return ActionResultType.PASS;
		}
		final TrackBuilderManager manager = new TrackBuilderManager(context.getPos(), context.getFace(), world, player.getLookVec());
		
		if (!manager.calculateBlockPosition()) {
			return ActionResultType.PASS;
		}
		
		manager.execute(player, new TrackBuilderInventoryWrapper.Server(context.getItem(), () -> player.world));
		
		return super.onItemUse(context);
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return !ItemStack.areItemsEqual(oldStack, newStack);
	}
}
