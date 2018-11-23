package info.u_team.useful_railroads.tilentity;

import info.u_team.u_team_core.tileentity.UTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityRailTeleport extends UTileEntity {
	
	private int fuel = 0;
	private int needFuel = 0;
	private int dimension = 0;
	private BlockPos teleportPos = new BlockPos(0, -1, 0);
	
	@Override
	public void readNBT(NBTTagCompound compound) {
		fuel = compound.getInteger("fuel");
		needFuel = compound.getInteger("need_fuel");
		dimension = compound.getInteger("tel_dim");
		int x = compound.getInteger("tel_x");
		int y = compound.getInteger("tel_y");
		int z = compound.getInteger("tel_z");
		teleportPos = new BlockPos(x, y, z);
	}
	
	@Override
	public void writeNBT(NBTTagCompound compound) {
		compound.setInteger("fuel", fuel);
		compound.setInteger("need_fuel", needFuel);
		compound.setInteger("tel_dim", dimension);
		compound.setInteger("tel_x", teleportPos.getX());
		compound.setInteger("tel_y", teleportPos.getY());
		compound.setInteger("tel_z", teleportPos.getZ());
	}
	
	/*
	 * Don't break tile entity when power or direction is updated
	 */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	public void setLocation(int dimension, BlockPos teleportPos) {
		this.dimension = dimension;
		this.teleportPos = teleportPos;
		needFuel = calculateFuel();
	}
	
	public void teleport(World world, EntityMinecart cart, BlockPos pos) {
		cart.motionX = 0;
		cart.motionY = 0;
		cart.motionZ = 0;
		
		System.out.println(teleportPos);
		System.out.println(dimension);
		
		Entity entity = cart.getPassengers().isEmpty() ? null : (Entity) cart.getPassengers().get(0);
		
		if (teleportPos.getY() < 0) {
			if (entity instanceof EntityPlayerMP)
				((EntityPlayerMP) entity).sendStatusMessage(new TextComponentString("§4You have not setup the rail yet."), true);
			return;
		}
		
		//TODO implement fuel etc
		
//		if (fuel < needFuel) {
//			if (entity instanceof EntityPlayerMP)
//				((EntityPlayerMP) entity).sendStatusMessage(new TextComponentString("§4Fuel missing! Need: §6" + needFuel + "§4. Have: §6" + fuel), true);
//			return;
//		}
		
		fuel -= needFuel;
		int currentDim = world.provider.getDimension();
		if (currentDim == dimension) {
			cart.setLocationAndAngles(teleportPos.getX(), teleportPos.getY(), teleportPos.getZ(), cart.rotationYaw, cart.rotationPitch);
		} else {
			ITeleporter teleporter = (world2, entity2, yaw) -> entity2.setLocationAndAngles(teleportPos.getX(), teleportPos.getY(), teleportPos.getZ(), entity2.rotationYaw, entity2.rotationPitch);
			
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			PlayerList list = server.getPlayerList();
			
			if (entity != null) {
				if (entity instanceof EntityPlayerMP) {
					list.transferPlayerToDimension((EntityPlayerMP) entity, dimension, teleporter);
				} else {
					list.transferEntityToWorld(entity, currentDim, server.getWorld(currentDim), server.getWorld(dimension), teleporter);
				}
			}
			list.transferEntityToWorld(cart, currentDim, server.getWorld(currentDim), server.getWorld(dimension), teleporter);
		}
	}
	
	private int calculateFuel() {
		if (!hasWorld()) {
			return 0;
		}
		int needFuel = 0;
		
		int currentDim = world.provider.getDimension();
		if (currentDim != dimension) {
			needFuel += 50;
		}
		double calculatedDistance = Math.log(pos.distanceSq(teleportPos)) / Math.log(5);
		calculatedDistance = calculatedDistance * calculatedDistance;
		needFuel += MathHelper.floor(calculatedDistance);
		return needFuel;
	}
	
}
