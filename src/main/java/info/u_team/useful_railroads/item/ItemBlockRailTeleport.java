package info.u_team.useful_railroads.item;

import java.util.List;

import info.u_team.u_team_core.util.MathUtil;
import info.u_team.useful_railroads.tilentity.TileEntityRailTeleport;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextFormatting;
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
		if (ourStack.hasTagCompound() && ourStack.getTagCompound().hasKey("dim")) {
			return false;
		}
		World world = entityitem.getEntityWorld();
		if (world.isRemote) {
			if (world.rand.nextInt(10) == 0) {
				for (int i = 0; i < 5; i++) {
					world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, true, entityitem.posX, entityitem.posY + 0.5, entityitem.posZ, MathUtil.getRandomNumberInRange(world.rand, -0.2, 0.2), MathUtil.getRandomNumberInRange(world.rand, 0.1, 1.5), MathUtil.getRandomNumberInRange(world.rand, -0.2, 0.2));
				}
			}
		} else {
			if (entityitem.age < 100) {
				return false;
			}
			AxisAlignedBB aabb = new AxisAlignedBB(entityitem.posX - 1, entityitem.posY - 1, entityitem.posZ - 1, entityitem.posX + 1, entityitem.posY + 1, entityitem.posZ + 1);
			world.getEntitiesWithinAABBExcludingEntity(entityitem, aabb).stream().filter(entity -> entity instanceof EntityItem).map(entity -> (EntityItem) entity).filter(entity -> entity.getAge() >= 100).forEach(entity -> {
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
						
						entityitem.setItem(ourStack); // Send changes to client
						
						world.addWeatherEffect(new EntityLightningBolt(world, entityitem.posX, entityitem.posY, entityitem.posZ, true));
					}
				}
			});
		}
		return false;
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("dim")) {
			return;
		}
		NBTTagCompound compound = stack.getTagCompound();
		tooltip.add("Dimension: " + TextFormatting.DARK_GREEN + compound.getInteger("dim"));
		tooltip.add("X: " + TextFormatting.DARK_GREEN + compound.getInteger("x"));
		tooltip.add("Y: " + TextFormatting.DARK_GREEN + compound.getInteger("y"));
		tooltip.add("Z: " + TextFormatting.DARK_GREEN + compound.getInteger("z"));
		if (compound.hasKey("fuel")) {
			tooltip.add("");
			tooltip.add("Fuel: " + TextFormatting.DARK_AQUA + compound.getInteger("fuel"));
		}
		super.addInformation(stack, world, tooltip, flag);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		IBlockState state = null;
		if (world.setBlockState(pos, newState, 11)) {
			state = world.getBlockState(pos);
			if (state.getBlock() == this.block) {
				block.onBlockPlacedBy(world, pos, state, player, stack);
				
				if (player instanceof EntityPlayerMP) {
					CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
				}
			}
		} else {
			return true;
		}
		
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
		
		if (compound.hasKey("fuel")) {
			rail.setFuel(compound.getInteger("fuel"));
		}
		
		// Sync te to client.
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, state, state, 3);
		world.scheduleBlockUpdate(pos, state.getBlock(), 0, 0);
		
		rail.markDirty();
		return true;
	}
	
}
