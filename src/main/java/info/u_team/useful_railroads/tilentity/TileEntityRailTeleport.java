package info.u_team.useful_railroads.tilentity;

import info.u_team.u_team_core.tileentity.UTileEntity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class TileEntityRailTeleport extends UTileEntity {
	
	private int fuel;
	private int needFuel;
	private int dimension = 0;
	private BlockPos teleportPos = BlockPos.ORIGIN;
	
	@Override
	public void readNBT(NBTTagCompound compound) {
		fuel = compound.getInteger("fuel");
		needFuel = compound.getInteger("need_fuel");
		dimension = compound.getInteger("dim");
		int x = compound.getInteger("x");
		int y = compound.getInteger("y");
		int z = compound.getInteger("z");
		teleportPos = new BlockPos(x, y, z);
	}
	
	@Override
	public void writeNBT(NBTTagCompound compound) {
		compound.setInteger("fuel", fuel);
		compound.setInteger("need_fuel", needFuel);
		compound.setInteger("dim", dimension);
		compound.setInteger("x", teleportPos.getX());
		compound.setInteger("y", teleportPos.getX());
		compound.setInteger("z", teleportPos.getX());
	}
	
	public int getNeedFuel() {
		return needFuel;
	}
	
	private void setNeedFuel(int needFuel) {
		this.needFuel = needFuel;
	}
	
	public int getFuel() {
		return fuel;
	}
	
	private void setFuel(int fuel) {
		this.fuel = fuel;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	private void setDimension(int dimension) {
		this.dimension = dimension;
	}
	
	public BlockPos getTeleportPos() {
		return teleportPos;
	}
	
	private void setTeleportPos(BlockPos teleportPos) {
		this.teleportPos = teleportPos;
	}
	
	public void teleport(World world, EntityMinecart cart, BlockPos pos) {
		
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
