package info.u_team.useful_railroads.tileentity;

import info.u_team.u_team_core.api.sync.IInitSyncedTileEntity;
import info.u_team.u_team_core.tileentity.UTileEntity;
import info.u_team.u_team_core.util.world.WorldUtil;
import info.u_team.useful_railroads.config.CommonConfig;
import info.u_team.useful_railroads.container.TeleportRailContainer;
import info.u_team.useful_railroads.init.UsefulRailroadsRecipeTypes;
import info.u_team.useful_railroads.init.UsefulRailroadsTileEntityTypes;
import info.u_team.useful_railroads.inventory.FuelItemHandler;
import info.u_team.useful_railroads.recipe.TeleportRailFuelRecipe;
import info.u_team.useful_railroads.util.Location;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetPassengersPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class TeleportRailTileEntity extends UTileEntity implements IInitSyncedTileEntity {
	
	private final Location location = Location.getOrigin();
	
	private int fuel;
	private int cost;
	
	private final FuelItemHandler<TeleportRailFuelRecipe> fuelSlot = new FuelItemHandler<>(UsefulRailroadsRecipeTypes.TELEPORT_RAIL_FUEL, this::getWorld, () -> fuel < 10000, fuelAdder -> {
		fuel += fuelAdder;
		markDirty();
	});
	
	private final LazyOptional<FuelItemHandler<TeleportRailFuelRecipe>> fuelSlotOptional = LazyOptional.of(() -> fuelSlot);
	
	public TeleportRailTileEntity() {
		super(UsefulRailroadsTileEntityTypes.TELEPORT_RAIL.get());
	}
	
	private void checkCost() {
		if (cost == 0) { // Calculate cost if not already done
			cost = calculateCost();
		}
	}
	
	private int calculateCost() {
		int calculatedCost = 0;
		
		if (!location.getRegistryKey().equals(world.getDimensionKey())) {
			calculatedCost += CommonConfig.getInstance().teleportRailDimensionCost.get();
		}
		double calculatedDistance = Math.log(pos.distanceSq(location.getPos())) / Math.log(CommonConfig.getInstance().teleportRailLogDivisionCost.get());
		calculatedDistance = calculatedDistance * calculatedDistance;
		calculatedCost += MathHelper.floor(calculatedDistance);
		if (calculatedCost == 0) {
			return 1;
		}
		return calculatedCost;
	}
	
	public void teleport(BlockPos pos, AbstractMinecartEntity cart) {
		checkCost();
		
		// Reset motion
		cart.setMotion(0, 0, 0);
		
		final Entity entity = cart.getPassengers().isEmpty() ? null : (Entity) cart.getPassengers().get(0);
		
		// Check fuel
		if (fuel < cost) {
			if (entity instanceof PlayerEntity) {
				((PlayerEntity) entity).sendStatusMessage(new TranslationTextComponent("block.usefulrailroads.teleport_rail.not_enough_fuel", cost).mergeStyle(TextFormatting.RED), true);
			}
			return;
		}
		fuel -= cost;
		markDirty();
		
		// Teleportation process
		final ServerWorld teleportWorld = cart.getServer().getWorld(location.getRegistryKey());
		
		if (teleportWorld == null) {
			return;
		}
		
		// Enqueue the teleportation to be executed after the ticks of entites because
		// else the teleportation will crash
		cart.getServer().enqueue(new TickDelayedTask(0, () -> {
			final Vector3d teleportPos = Vector3d.copyCentered(location.getPos());
			
			// Teleport minecart
			WorldUtil.teleportEntity(cart, teleportWorld, teleportPos);
			
			// Teleport entity riding if there is one
			if (entity != null) {
				WorldUtil.teleportEntity(entity, teleportWorld, teleportPos);
				
				// Reatach entity because the entity will be destroyed when changing dimensions we use the uuid
				final Entity newCart = teleportWorld.getEntityByUuid(cart.getUniqueID());
				entity.startRiding(newCart, true);
				
				// For some reason the entity tracker does not update the passengers to the client so we send the packet manually.
				// See MC-U-Team/Useful-Railroads#21
				teleportWorld.getChunkProvider().sendToTrackingAndSelf(entity, new SSetPassengersPacket(newCart));
			}
		}));
		
	}
	
	@Override
	public void writeNBT(CompoundNBT compound) {
		compound.put("location", location.serializeNBT());
		if (fuel != 0) { // Don't save fuel if it's 0
			compound.putInt("fuel", fuel);
		}
	}
	
	@Override
	public void readNBT(BlockState state, CompoundNBT compound) {
		location.deserializeNBT(compound.getCompound("location"));
		fuel = compound.getInt("fuel");
	}
	
	@Override
	public void sendInitialDataBuffer(PacketBuffer buffer) {
		checkCost();
		location.serialize(buffer);
		buffer.writeInt(fuel);
		buffer.writeInt(cost);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleInitialDataBuffer(PacketBuffer buffer) {
		location.deserialize(buffer);
		fuel = buffer.readInt();
		cost = buffer.readInt();
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction direction) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && direction != Direction.UP) {
			return fuelSlotOptional.cast();
		}
		return super.getCapability(capability, direction);
	}
	
	@Override
	public void remove() {
		super.remove();
		fuelSlotOptional.invalidate();
	}
	
	public FuelItemHandler<TeleportRailFuelRecipe> getFuelSlot() {
		return fuelSlot;
	}
	
	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
		return new TeleportRailContainer(id, playerInventory, this);
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.usefulrailroads.teleport_rail");
	}
	
	public int getFuel() {
		return fuel;
	}
	
	public void setFuel(int fuel) {
		this.fuel = fuel;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public int getCost() {
		return cost;
	}
}
