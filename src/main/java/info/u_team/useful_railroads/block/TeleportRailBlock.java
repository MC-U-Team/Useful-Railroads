package info.u_team.useful_railroads.block;

import java.util.List;

import info.u_team.useful_railroads.init.UsefulRailroadsTileEntityTypes;
import info.u_team.useful_railroads.item.TeleportRailBlockItem;
import info.u_team.useful_railroads.tileentity.TeleportRailTileEntity;
import net.minecraft.block.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.*;

public class TeleportRailBlock extends CustomTileEntityPoweredRailBlock {
	
	public TeleportRailBlock() {
		super(UsefulRailroadsTileEntityTypes.TELEPORT_RAIL);
	}
	
	@Override
	protected BlockItem createBlockItem(Item.Properties blockItemProperties) {
		return new TeleportRailBlockItem(this, blockItemProperties);
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		return openContainer(world, pos, player, true);
	}
	
	@Override
	public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		if (world.isRemote) {
			return;
		}
		if (!state.get(PoweredRailBlock.POWERED)) {
			return;
		}
		isTileEntityFromType(world, pos).map(TeleportRailTileEntity.class::cast).ifPresent(tileEntity -> tileEntity.teleport(pos, cart));
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isRemote && !player.isCreative()) {
			final ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), getItemStack(world, pos));
			itemEntity.setDefaultPickupDelay();
			world.addEntity(itemEntity);
		}
		super.onBlockHarvested(world, pos, state, player);
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
		return getItemStack(world, pos);
	}
	
	private ItemStack getItemStack(IBlockReader world, BlockPos pos) {
		final ItemStack stack = new ItemStack(this);
		isTileEntityFromType(world, pos).map(TeleportRailTileEntity.class::cast).ifPresent(tileEntity -> {
			final CompoundNBT compound = new CompoundNBT();
			tileEntity.writeNBT(compound);
			if (!compound.isEmpty()) {
				stack.setTagInfo("BlockEntityTag", compound);
			}
		});
		return stack;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		final CompoundNBT compound = stack.getChildTag("BlockEntityTag");
		final boolean compoundExists = compound != null;
		
		final String langKey = "container.usefulrailroads.teleport_rail.";
		final ITextComponent seperatorTextComponent = new StringTextComponent(": ");
		
		if (compoundExists && compound.contains("location")) {
			final CompoundNBT locationCompound = compound.getCompound("location");
			
			tooltip.add(new TranslationTextComponent(langKey + "dimension").append(seperatorTextComponent).append(new StringTextComponent(locationCompound.getString("dimension")).mergeStyle(TextFormatting.DARK_GREEN)));
			tooltip.add(new TranslationTextComponent(langKey + "x").append(seperatorTextComponent).append(new StringTextComponent(Integer.toString(locationCompound.getInt("x"))).mergeStyle(TextFormatting.DARK_GREEN)));
			tooltip.add(new TranslationTextComponent(langKey + "y").append(seperatorTextComponent).append(new StringTextComponent(Integer.toString(locationCompound.getInt("y"))).mergeStyle(TextFormatting.DARK_GREEN)));
			tooltip.add(new TranslationTextComponent(langKey + "z").append(seperatorTextComponent).append(new StringTextComponent(Integer.toString(locationCompound.getInt("z"))).mergeStyle(TextFormatting.DARK_GREEN)));
		} else {
			tooltip.add(new TranslationTextComponent("block.usefulrailroads.teleport_rail.missing_setup").mergeStyle(TextFormatting.RED));
			tooltip.add(new TranslationTextComponent("block.usefulrailroads.teleport_rail.how_to_setup").mergeStyle(TextFormatting.GRAY));
		}
		if (compoundExists) {
			tooltip.add(new TranslationTextComponent(langKey + "fuel").append(seperatorTextComponent).append(new StringTextComponent(Integer.toString(compound.getInt("fuel"))).mergeStyle(TextFormatting.DARK_AQUA)));
		}
	}
}
