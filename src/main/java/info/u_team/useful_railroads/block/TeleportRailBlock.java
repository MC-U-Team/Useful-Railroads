package info.u_team.useful_railroads.block;

import java.util.List;

import info.u_team.useful_railroads.init.UsefulRailroadsTileEntityTypes;
import info.u_team.useful_railroads.item.TeleportRailBlockItem;
import info.u_team.useful_railroads.tileentity.TeleportRailTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.*;

public class TeleportRailBlock extends CustomTileEntityPoweredRailBlock {
	
	public TeleportRailBlock(String name) {
		super(name, () -> UsefulRailroadsTileEntityTypes.TELEPORT_RAIL);
	}
	
	@Override
	protected BlockItem createBlockItem(Item.Properties blockItemProperties) {
		return new TeleportRailBlockItem(this, blockItemProperties);
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		return openContainer(world, pos, player, true);
	}
	
	@Override
	public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		super.onMinecartPass(state, world, pos, cart);
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
		final TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TeleportRailTileEntity) {
			final TeleportRailTileEntity teleportRailTileEntity = (TeleportRailTileEntity) tileEntity;
			final CompoundNBT compound = new CompoundNBT();
			teleportRailTileEntity.writeNBT(compound);
			if (!compound.isEmpty()) {
				stack.setTagInfo("BlockEntityTag", compound);
			}
		}
		return stack;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		final CompoundNBT compound = stack.getChildTag("BlockEntityTag");
		final boolean compoundExists = compound != null;
		if (compoundExists && compound.contains("location")) {
			final CompoundNBT locationCompound = compound.getCompound("location");
			tooltip.add(new StringTextComponent("Dimension: " + TextFormatting.DARK_GREEN + locationCompound.getString("dimension")));
			tooltip.add(new StringTextComponent("X: " + TextFormatting.DARK_GREEN + locationCompound.getInt("x")));
			tooltip.add(new StringTextComponent("Y: " + TextFormatting.DARK_GREEN + locationCompound.getInt("y")));
			tooltip.add(new StringTextComponent("Z: " + TextFormatting.DARK_GREEN + locationCompound.getInt("z")));
		} else {
			tooltip.add(new StringTextComponent(TextFormatting.DARK_RED + "This rail is not setuped yet."));
		}
		if (compoundExists) {
			tooltip.add(new StringTextComponent("Fuel: " + TextFormatting.DARK_AQUA + compound.getInt("fuel")));
		}
	}
}
