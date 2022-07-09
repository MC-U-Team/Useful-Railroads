package info.u_team.useful_railroads.block;

import java.util.List;

import info.u_team.useful_railroads.blockentity.TeleportRailBlockEntity;
import info.u_team.useful_railroads.init.UsefulRailroadsBlockEntityTypes;
import info.u_team.useful_railroads.item.TeleportRailBlockItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class TeleportRailBlock extends CustomBlockEntityPoweredRailBlock {
	
	public TeleportRailBlock() {
		super(UsefulRailroadsBlockEntityTypes.TELEPORT_RAIL);
	}
	
	@Override
	protected BlockItem createBlockItem(Item.Properties blockItemProperties) {
		return new TeleportRailBlockItem(this, blockItemProperties);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		return openMenu(level, pos, player, true);
	}
	
	@Override
	public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
		if (level.isClientSide || !state.getValue(PoweredRailBlock.POWERED)) {
			return;
		}
		getBlockEntity(level, pos).map(TeleportRailBlockEntity.class::cast).ifPresent(tileEntity -> tileEntity.teleport(pos, cart));
	}
	
	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide && !player.isCreative()) {
			final ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), getItemStack(level, pos));
			itemEntity.setDefaultPickUpDelay();
			level.addFreshEntity(itemEntity);
		}
		super.playerWillDestroy(level, pos, state, player);
	}
	
	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		return getItemStack(level, pos);
	}
	
	private ItemStack getItemStack(BlockGetter level, BlockPos pos) {
		final ItemStack stack = new ItemStack(this);
		getBlockEntity(level, pos).map(TeleportRailBlockEntity.class::cast).ifPresent(tileEntity -> {
			final CompoundTag compound = new CompoundTag();
			tileEntity.saveNBT(compound);
			if (!compound.isEmpty()) {
				stack.addTagElement("BlockEntityTag", compound);
			}
		});
		return stack;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
		final CompoundTag compound = stack.getTagElement("BlockEntityTag");
		final boolean compoundExists = compound != null;
		
		final String langKey = "container.usefulrailroads.teleport_rail.";
		final Component seperatorTextComponent = Component.literal(": ");
		
		if (compoundExists && compound.contains("location")) {
			final CompoundTag locationCompound = compound.getCompound("location");
			
			tooltip.add(Component.translatable(langKey + "dimension").append(seperatorTextComponent).append(Component.literal(locationCompound.getString("dimension")).withStyle(ChatFormatting.DARK_GREEN)));
			tooltip.add(Component.translatable(langKey + "x").append(seperatorTextComponent).append(Component.literal(Integer.toString(locationCompound.getInt("x"))).withStyle(ChatFormatting.DARK_GREEN)));
			tooltip.add(Component.translatable(langKey + "y").append(seperatorTextComponent).append(Component.literal(Integer.toString(locationCompound.getInt("y"))).withStyle(ChatFormatting.DARK_GREEN)));
			tooltip.add(Component.translatable(langKey + "z").append(seperatorTextComponent).append(Component.literal(Integer.toString(locationCompound.getInt("z"))).withStyle(ChatFormatting.DARK_GREEN)));
		} else {
			tooltip.add(Component.translatable("block.usefulrailroads.teleport_rail.missing_setup").withStyle(ChatFormatting.RED));
			tooltip.add(Component.translatable("block.usefulrailroads.teleport_rail.how_to_setup").withStyle(ChatFormatting.GRAY));
		}
		if (compoundExists) {
			tooltip.add(Component.translatable(langKey + "fuel").append(seperatorTextComponent).append(Component.literal(Integer.toString(compound.getInt("fuel"))).withStyle(ChatFormatting.DARK_AQUA)));
		}
	}
}
