package info.u_team.useful_railroads.item;

import java.util.List;

import info.u_team.u_team_core.item.UItem;
import info.u_team.useful_railroads.config.ServerConfig;
import info.u_team.useful_railroads.container.TrackBuilderContainer;
import info.u_team.useful_railroads.init.UsefulRailroadsItemGroups;
import info.u_team.useful_railroads.inventory.TrackBuilderInventoryWrapper;
import info.u_team.useful_railroads.util.TrackBuilderManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.fml.network.NetworkHooks;

public class TrackBuilderItem extends UItem {
	
	private final boolean doubleTrack;
	
	public TrackBuilderItem(boolean doubleTrack) {
		super(UsefulRailroadsItemGroups.GROUP, new Properties().maxStackSize(1).rarity(doubleTrack ? Rarity.EPIC : Rarity.RARE));
		this.doubleTrack = doubleTrack;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		final ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote && !player.isSneaking() && player instanceof ServerPlayerEntity) { // Server & Player not sneaking
			final TrackBuilderInventoryWrapper wrapper = new TrackBuilderInventoryWrapper.Server(stack, () -> player.world);
			final int selectedSlot = hand == Hand.MAIN_HAND ? player.inventory.currentItem : -1;
			
			NetworkHooks.openGui((ServerPlayerEntity) player, new SimpleNamedContainerProvider((id, playerInventory, openPlayer) -> {
				return new TrackBuilderContainer(id, playerInventory, wrapper, selectedSlot);
			}, new TranslationTextComponent("container.usefulrailroads.track_builder")), buffer -> {
				buffer.writeVarInt(wrapper.getFuel());
				buffer.writeEnumValue(wrapper.getMode());
				buffer.writeVarInt(selectedSlot);
			});
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
		if (player == null || !context.func_225518_g_() || context.getHand() == Hand.OFF_HAND) { // No player or no sneaking and no offhand
			return ActionResultType.PASS;
		}
		final TrackBuilderInventoryWrapper wrapper = new TrackBuilderInventoryWrapper.Server(context.getItem(), () -> player.world);
		
		TrackBuilderManager.create(context.getPos(), context.getFace(), world, player.getLookVec(), wrapper.getMode(), doubleTrack).ifPresent(manager -> {
			manager.execute(player, wrapper);
		});
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return !ItemStack.areItemsEqual(oldStack, newStack);
	}
	
	@Override
	public CompoundNBT getShareTag(ItemStack stack) {
		if (ServerConfig.getInstance().shareAllNBTData.get()) {
			return super.getShareTag(stack);
		}
		if (!stack.hasTag()) {
			return null;
		}
		final CompoundNBT compound = stack.getTag().copy();
		compound.remove("Items");
		if (compound.isEmpty()) {
			return null;
		}
		return compound;
	}
	
	@Override
	public boolean onDroppedByPlayer(ItemStack item, PlayerEntity player) {
		return !(player.openContainer instanceof TrackBuilderContainer);
	}
	
	public boolean isDoubleTrack() {
		return doubleTrack;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		tooltip.add(new TranslationTextComponent(getTranslationKey() + ".tooltip.0", new TranslationTextComponent("usefulrailroads.tooltip.right_click").mergeStyle(TextFormatting.ITALIC, TextFormatting.GOLD)).mergeStyle(TextFormatting.GRAY));
		tooltip.add(new TranslationTextComponent(getTranslationKey() + ".tooltip.1", new TranslationTextComponent("usefulrailroads.tooltip.shift_right_click").mergeStyle(TextFormatting.ITALIC, TextFormatting.GOLD)).mergeStyle(TextFormatting.GRAY));
	}
	
}
