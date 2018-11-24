package info.u_team.useful_railroads.tilentity;

import java.util.HashMap;

import info.u_team.u_team_core.tileentity.UTileEntity;
import info.u_team.useful_railroads.config.CommonConfig;
import info.u_team.useful_railroads.inventory.InvWrapperOnlyInsert;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TileEntityRailTeleport extends UTileEntity implements IInventory, ITickable {
	
	protected HashMap<String, Integer> fuelList;
	
	protected int fuel = 0;
	protected int needFuel = 0;
	protected int dimension = 0;
	protected BlockPos teleportPos = new BlockPos(0, -1, 0);
	
	protected NonNullList<ItemStack> itemstacks;
	
	protected InvWrapper inventoryWrapper;
	
	public TileEntityRailTeleport() {
		itemstacks = NonNullList.withSize(1, ItemStack.EMPTY);
		inventoryWrapper = new InvWrapperOnlyInsert(this);
		fuelList = CommonConfig.teleportrail.fuel;
	}
	
	@Override
	public void readNBT(NBTTagCompound compound) {
		fuel = compound.getInteger("fuel");
		needFuel = compound.getInteger("need_fuel");
		dimension = compound.getInteger("tel_dim");
		int x = compound.getInteger("tel_x");
		int y = compound.getInteger("tel_y");
		int z = compound.getInteger("tel_z");
		teleportPos = new BlockPos(x, y, z);
		
		ItemStackHelper.loadAllItems(compound, itemstacks);
	}
	
	@Override
	public void writeNBT(NBTTagCompound compound) {
		compound.setInteger("fuel", fuel);
		compound.setInteger("need_fuel", needFuel);
		compound.setInteger("tel_dim", dimension);
		compound.setInteger("tel_x", teleportPos.getX());
		compound.setInteger("tel_y", teleportPos.getY());
		compound.setInteger("tel_z", teleportPos.getZ());
		
		ItemStackHelper.saveAllItems(compound, itemstacks, false);
	}
	
	public void setLocation(int dimension, BlockPos teleportPos) {
		this.dimension = dimension;
		this.teleportPos = teleportPos;
		needFuel = calculateFuel();
	}
	
	public void setFuel(int fuel) {
		this.fuel = fuel;
	}
	
	public int getFuel() {
		return fuel;
	}
	
	public void teleport(World world, EntityMinecart cart, BlockPos pos) {
		cart.motionX = 0;
		cart.motionY = 0;
		cart.motionZ = 0;
		
		Entity entity = cart.getPassengers().isEmpty() ? null : (Entity) cart.getPassengers().get(0);
		
		if (teleportPos.getY() < 0) {
			if (entity instanceof EntityPlayerMP) {
				((EntityPlayerMP) entity).sendStatusMessage(new TextComponentString("§4You have not setup the rail yet."), true);
			}
			return;
		}
		
		if (fuel < needFuel) {
			if (entity instanceof EntityPlayerMP) {
				((EntityPlayerMP) entity).sendStatusMessage(new TextComponentString("§4Fuel missing! Need: §6" + needFuel + "§4. Have: §6" + fuel), true);
			}
			return;
		}
		
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
		
		// Sync te to client.
		world.markBlockRangeForRenderUpdate(pos, pos);
		IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
		world.scheduleBlockUpdate(pos, blockType, 0, 0);
		markDirty();
	}
	
	private int calculateFuel() {
		if (!hasWorld()) {
			return 0;
		}
		int needFuel = 0;
		
		int currentDim = world.provider.getDimension();
		if (currentDim != dimension) {
			needFuel += 100;
		}
		double calculatedDistance = Math.log(pos.distanceSq(teleportPos)) / Math.log(5);
		calculatedDistance = calculatedDistance * calculatedDistance;
		needFuel += MathHelper.floor(calculatedDistance);
		return needFuel;
	}
	
	@Override
	public void update() {
		ItemStack stack = itemstacks.get(0);
		if (stack.isEmpty()) {
			return;
		}
		String registryname = stack.getItem().getRegistryName().toString();
		int value = fuelList.getOrDefault(registryname, 0);
		if (value > 0) {
			fuel += value * stack.getCount();
			itemstacks.clear();
		}
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}
		String registryname = stack.getItem().getRegistryName().toString();
		return fuelList.getOrDefault(registryname, 0) > 0;
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock(); // Don't break tile entity when power or direction is updated
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != EnumFacing.UP) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != EnumFacing.UP) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventoryWrapper);
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public String getName() {
		return "railteleport";
	}
	
	@Override
	public boolean hasCustomName() {
		return false;
	}
	
	@Override
	public int getSizeInventory() {
		return itemstacks.size();
	}
	
	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : itemstacks) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public ItemStack getStackInSlot(int index) {
		return itemstacks.get(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(itemstacks, index, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(itemstacks, index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		itemstacks.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}
	
	@Override
	public void openInventory(EntityPlayer player) {
	}
	
	@Override
	public void closeInventory(EntityPlayer player) {
	}
	
	@Override
	public void clear() {
		itemstacks.clear();
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public BlockPos getTeleportPos() {
		return teleportPos;
	}
	
	@SideOnly(Side.CLIENT)
	private int client_dim, client_x, client_y, client_z, client_fuel, client_needFuel;
	
	@SideOnly(Side.CLIENT)
	public int getClientDim() {
		return client_dim;
	}
	
	@SideOnly(Side.CLIENT)
	public int getClientX() {
		return client_x;
	}
	
	@SideOnly(Side.CLIENT)
	public int getClientY() {
		return client_y;
	}
	
	@SideOnly(Side.CLIENT)
	public int getClientZ() {
		return client_z;
	}
	
	@SideOnly(Side.CLIENT)
	public int getClientFuel() {
		return client_fuel;
	}
	
	@SideOnly(Side.CLIENT)
	public int getClientNeedFuel() {
		return client_needFuel;
	}
	
	@Override
	public void setField(int id, int value) {
		switch (id) {
		case 0:
			client_dim = value;
			break;
		case 1:
			client_x = value;
			break;
		case 2:
			client_y = value;
			break;
		case 3:
			client_z = value;
			break;
		case 4:
			client_fuel = value;
			break;
		case 5:
			client_needFuel = value;
			break;
		}
	}
	
	@Override
	public int getField(int id) {
		switch (id) {
		case 0:
			return dimension;
		case 1:
			return teleportPos.getX();
		case 2:
			return teleportPos.getY();
		case 3:
			return teleportPos.getZ();
		case 4:
			return fuel;
		case 5:
			return needFuel;
		default:
			return 0;
		}
	}
	
	@Override
	public int getFieldCount() {
		return 6;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, -1, this.getUpdateTag());
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}
	
}
