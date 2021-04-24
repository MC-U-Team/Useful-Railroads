package info.u_team.useful_railroads.block;

import java.util.List;

import info.u_team.useful_railroads.init.UsefulRailroadsTileEntityTypes;
import info.u_team.useful_railroads.item.TeleportRailBlockItem;
import info.u_team.useful_railroads.tileentity.TeleportRailTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
			
			tooltip.add(new TranslationTextComponent(langKey + "dimension").appendSibling(seperatorTextComponent).appendSibling(new StringTextComponent(locationCompound.getString("dimension")).mergeStyle(TextFormatting.DARK_GREEN)));
			tooltip.add(new TranslationTextComponent(langKey + "x").appendSibling(seperatorTextComponent).appendSibling(new StringTextComponent(Integer.toString(locationCompound.getInt("x"))).mergeStyle(TextFormatting.DARK_GREEN)));
			tooltip.add(new TranslationTextComponent(langKey + "y").appendSibling(seperatorTextComponent).appendSibling(new StringTextComponent(Integer.toString(locationCompound.getInt("y"))).mergeStyle(TextFormatting.DARK_GREEN)));
			tooltip.add(new TranslationTextComponent(langKey + "z").appendSibling(seperatorTextComponent).appendSibling(new StringTextComponent(Integer.toString(locationCompound.getInt("z"))).mergeStyle(TextFormatting.DARK_GREEN)));
		} else {
			tooltip.add(new TranslationTextComponent("block.usefulrailroads.teleport_rail.missing_setup").mergeStyle(TextFormatting.RED));
			tooltip.add(new TranslationTextComponent("block.usefulrailroads.teleport_rail.how_to_setup").mergeStyle(TextFormatting.GRAY));
		}
		if (compoundExists) {
			tooltip.add(new TranslationTextComponent(langKey + "fuel").appendSibling(seperatorTextComponent).appendSibling(new StringTextComponent(Integer.toString(compound.getInt("fuel"))).mergeStyle(TextFormatting.DARK_AQUA)));
		}
	}
}
