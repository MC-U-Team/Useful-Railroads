package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.tileentity.UTileEntityProvider;
import info.u_team.useful_railroads.UsefulRailroadsConstants;
import info.u_team.useful_railroads.item.ItemBlockRailTeleport;
import info.u_team.useful_railroads.tilentity.TileEntityRailTeleport;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class BlockRailTeleport extends BlockTileEntityCustomRailPowered {
	
	public BlockRailTeleport(String name) {
		super(name, new UTileEntityProvider(new ResourceLocation(UsefulRailroadsConstants.MODID, name + "_tile"), TileEntityRailTeleport.class));
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		if (player.isSneaking()) {
			return false;
		}
		TileEntity tile = world.getTileEntity(pos);
		if (!(tile instanceof TileEntityRailTeleport)) {
			return false;
		}
		TileEntityRailTeleport rail = (TileEntityRailTeleport) tile;
		
		if (rail.getTeleportPos().getY() < 0) {
			player.sendMessage(new TextComponentString("§4You have not setup the rail yet."));
			return true;
		}
		
		player.openGui(UsefulRailroadsConstants.MODID, 0, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	@Override
	public void onMinecartPassPowered(World world, EntityMinecart cart, BlockPos pos) {
		if (world.isRemote) {
			return;
		}
		TileEntity tile = world.getTileEntity(pos);
		if (!(tile instanceof TileEntityRailTeleport)) {
			return;
		}
		TileEntityRailTeleport rail = (TileEntityRailTeleport) tile;
		rail.teleport(world, cart, pos);
	}
	
	@Override
	public ItemBlock getItemBlock() {
		return new ItemBlockRailTeleport(this);
	}
	
	/*
	 * This method is only called from the minecraft class, so we set @SideOnly to
	 * client only. When mods missuse this method its not our problem!
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		ItemStack stack = new ItemStack(getItem());
		
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityRailTeleport && world.isRemote) {
			TileEntityRailTeleport rail = (TileEntityRailTeleport) tile;
			stack.setTagCompound(getTag(rail)); // Only works, cause of sync when placed
		}
		return stack;
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (!player.isCreative()) {
			if (!world.isRemote) {
				harvestBlock(world, player, pos, state, null, player.getHeldItemMainhand());
			}
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), world.isRemote ? 11 : 3);
			return false;
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}
	
	@Override
	public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune) {
		if (!world.isRemote && !world.restoringBlockSnapshots) {
			ItemStack stack = new ItemStack(getItem());
			
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileEntityRailTeleport) {
				TileEntityRailTeleport rail = (TileEntityRailTeleport) tile;
				stack.setTagCompound(getTag(rail));
			}
			
			spawnAsEntity(world, pos, stack);
		}
	}
	
	public NBTTagCompound getTag(TileEntityRailTeleport rail) {
		NBTTagCompound compound = new NBTTagCompound();
		
		compound.setInteger("dim", rail.getDimension());
		BlockPos teleportPos = rail.getTeleportPos();
		compound.setInteger("x", teleportPos.getX());
		compound.setInteger("y", teleportPos.getY());
		compound.setInteger("z", teleportPos.getZ());
		
		if (rail.getFuel() > 0) {
			compound.setInteger("fuel", rail.getFuel());
		}
		
		return compound;
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return false;
	}
}
