package info.u_team.useful_railroads.item;

import java.util.List;

import info.u_team.useful_railroads.tilentity.TileEntityRailTeleport;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class ItemBlockRailTeleport extends ItemBlock {
	
	public ItemBlockRailTeleport(Block block) {
		super(block);
		setRegistryName(block.getRegistryName());
		setMaxStackSize(1);
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityitem) {
		ItemStack ourStack = entityitem.getItem();
		if (entityitem.getAge() < 100 || (ourStack.hasTagCompound() && ourStack.getTagCompound().hasKey("dim"))) {
			return false;
		}
		World world = entityitem.getEntityWorld();
		if (world.isRemote) {
			// TODO client tings etc
		} else {
			AxisAlignedBB aabb = new AxisAlignedBB(entityitem.posX - 1, entityitem.posY - 1, entityitem.posZ - 1, entityitem.posX + 1, entityitem.posY + 1, entityitem.posZ + 1);
			world.getEntitiesWithinAABBExcludingEntity(entityitem, aabb).stream().filter(entity -> entity instanceof EntityItem).map(entity -> (EntityItem) entity).forEach(entity -> {
				ItemStack stack = entity.getItem();
				if (!stack.isEmpty()) {
					Item item = stack.getItem();
					if (item instanceof ItemEnderPearl) {
						stack.shrink(1);
						BlockPos pos = entityitem.getPosition();
						if (!ourStack.hasTagCompound()) {
							ourStack.setTagCompound(new NBTTagCompound());
						}
						NBTTagCompound compound = ourStack.getTagCompound();
						compound.setInteger("dim", world.provider.getDimension());
						compound.setInteger("x", pos.getX());
						compound.setInteger("y", pos.getY());
						compound.setInteger("z", pos.getZ());
					}
				}
			});
		}
		return false;
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("dim")) {
			return;
		}
		NBTTagCompound compound = stack.getTagCompound();
		tooltip.add("Dimension: " + compound.getInteger("dim"));
		tooltip.add("X: " + compound.getInteger("x"));
		tooltip.add("Y: " + compound.getInteger("y"));
		tooltip.add("Z: " + compound.getInteger("z"));
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
		if (world.isRemote) {
			return true;
		}
		if (world.getBlockState(pos).getBlock() != this.block) {
			return true;
		}
		if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("dim")) {
			return true;
		}
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity == null || !(tileentity instanceof TileEntityRailTeleport)) {
			return true;
		}
		TileEntityRailTeleport rail = (TileEntityRailTeleport) tileentity;
		
		NBTTagCompound compound = stack.getTagCompound();
		
		rail.setLocation(compound.getInteger("dim"), new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z")));
		rail.markDirty();
		return true;
	}
	
}
