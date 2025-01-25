package info.u_team.useful_railroads.item;

import java.util.List;

import info.u_team.u_team_core.item.UItem;
import info.u_team.u_team_core.util.MenuUtil;
import info.u_team.u_team_core.util.TooltipCreator;
import info.u_team.useful_railroads.UsefulRailroadsReference;
import info.u_team.useful_railroads.component.TrackBuilderComponent;
import info.u_team.useful_railroads.init.UsefulRailroadsDataComponentTypes;
import info.u_team.useful_railroads.inventory.TrackBuilderInventoryWrapper;
import info.u_team.useful_railroads.menu.TrackBuilderMenu;
import info.u_team.useful_railroads.util.TrackBuilderManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class TrackBuilderItem extends UItem {
	
	private final boolean doubleTrack;
	
	public TrackBuilderItem(boolean doubleTrack) {
		super(new Properties().stacksTo(1).rarity(doubleTrack ? Rarity.EPIC : Rarity.RARE).component(UsefulRailroadsDataComponentTypes.TRACK_BUILDER.get(), TrackBuilderComponent.EMPTY));
		this.doubleTrack = doubleTrack;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		final ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide && !player.isShiftKeyDown() && player instanceof final ServerPlayer serverPlayer) { // Server & Player not sneaking
			final TrackBuilderInventoryWrapper wrapper = new TrackBuilderInventoryWrapper.Server(stack, player::level);
			final int selectedSlot = hand == InteractionHand.MAIN_HAND ? player.getInventory().selected : -1;
			
			MenuUtil.openMenu(serverPlayer, new SimpleMenuProvider((id, playerInventory, openPlayer) -> {
				return new TrackBuilderMenu(id, playerInventory, wrapper, selectedSlot);
			}, Component.translatable("container.usefulrailroads.track_builder")), buffer -> {
				buffer.writeVarInt(wrapper.getFuel());
				buffer.writeEnum(wrapper.getMode());
				buffer.writeVarInt(selectedSlot);
			}, false);
		}
		return InteractionResultHolder.success(stack);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		final Level level = context.getLevel();
		if (level.isClientSide) { // Server side
			return InteractionResult.PASS;
		}
		final Player player = context.getPlayer();
		if (player == null || !context.isSecondaryUseActive() || context.getHand() == InteractionHand.OFF_HAND) { // No player or no sneaking and no offhand
			return InteractionResult.PASS;
		}
		final TrackBuilderInventoryWrapper wrapper = new TrackBuilderInventoryWrapper.Server(context.getItemInHand(), player::level);
		
		TrackBuilderManager.create(context.getClickedPos(), context.getClickedFace(), level, player.getLookAngle(), wrapper.getMode(), doubleTrack).ifPresent(manager -> {
			manager.execute(player, wrapper);
		});
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public boolean shouldPlayUpdateAnimation(ItemStack oldStack, ItemStack newStack) {
		return !ItemStack.isSameItem(oldStack, newStack);
	}
	
	@Override
	public boolean canBeDropped(ItemStack stack, Player player) {
		return !(player.containerMenu instanceof TrackBuilderMenu);
	}
	
	public boolean isDoubleTrack() {
		return doubleTrack;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(TooltipCreator.create(this, "", 0, TooltipCreator.create(UsefulRailroadsReference.MODID, "click", "right_click", 0).withStyle(ChatFormatting.ITALIC, ChatFormatting.GOLD)).withStyle(ChatFormatting.GRAY));
		tooltip.add(TooltipCreator.create(this, "", 1, TooltipCreator.create(UsefulRailroadsReference.MODID, "click", "shift_right_click", 0).withStyle(ChatFormatting.ITALIC, ChatFormatting.GOLD)).withStyle(ChatFormatting.GRAY));
	}
	
}
