package info.u_team.useful_railroads.blockentity;

import info.u_team.u_team_core.api.block.MenuSyncedBlockEntity;
import info.u_team.u_team_core.blockentity.UBlockEntity;
import info.u_team.u_team_core.util.LevelUtil;
import info.u_team.useful_railroads.config.CommonConfig;
import info.u_team.useful_railroads.init.UsefulRailroadsBlockEntityTypes;
import info.u_team.useful_railroads.init.UsefulRailroadsRecipeTypes;
import info.u_team.useful_railroads.inventory.FuelItemHandler;
import info.u_team.useful_railroads.menu.TeleportRailMenu;
import info.u_team.useful_railroads.recipe.TeleportRailFuelRecipe;
import info.u_team.useful_railroads.util.Location;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

public class TeleportRailBlockEntity extends UBlockEntity implements MenuSyncedBlockEntity {
	
	private final Location location = Location.getOrigin();
	
	private int fuel;
	private int cost;
	
	private final FuelItemHandler<TeleportRailFuelRecipe> fuelSlot = new FuelItemHandler<>(UsefulRailroadsRecipeTypes.TELEPORT_RAIL_FUEL.get(), this::getLevel, () -> fuel < 10000, fuelAdder -> {
		fuel += fuelAdder;
		setChanged();
	});
	
	private final LazyOptional<FuelItemHandler<TeleportRailFuelRecipe>> fuelSlotOptional = LazyOptional.of(() -> fuelSlot);
	
	public TeleportRailBlockEntity(BlockPos pos, BlockState state) {
		super(UsefulRailroadsBlockEntityTypes.TELEPORT_RAIL.get(), pos, state);
	}
	
	private void checkCost() {
		if (cost == 0) { // Calculate cost if not already done
			cost = calculateCost();
		}
	}
	
	private int calculateCost() {
		int calculatedCost = 0;
		
		if (!location.getResourceKey().equals(level.dimension())) {
			calculatedCost += CommonConfig.getInstance().teleportRailDimensionCost.get();
		}
		double calculatedDistance = Math.log(worldPosition.distSqr(location.getPos())) / Math.log(CommonConfig.getInstance().teleportRailLogDivisionCost.get());
		calculatedDistance = calculatedDistance * calculatedDistance;
		calculatedCost += Mth.floor(calculatedDistance);
		if (calculatedCost == 0) {
			return 1;
		}
		return calculatedCost;
	}
	
	public void teleport(BlockPos pos, AbstractMinecart cart) {
		checkCost();
		
		// Reset motion
		cart.setDeltaMovement(0, 0, 0);
		
		final Entity entity = cart.getFirstPassenger();
		
		// Check fuel
		if (fuel < cost) {
			if (entity instanceof final Player player) {
				player.displayClientMessage(Component.translatable("block.usefulrailroads.teleport_rail.not_enough_fuel", cost).withStyle(ChatFormatting.RED), true);
			}
			return;
		}
		fuel -= cost;
		setChanged();
		
		// Teleportation process
		final ServerLevel teleportLevel = cart.getServer().getLevel(location.getResourceKey());
		
		if (teleportLevel == null) {
			return;
		}
		
		// Enqueue the teleportation to be executed after the ticks of entites because
		// else the teleportation will crash
		cart.getServer().tell(new TickTask(0, () -> {
			final Vec3 teleportPos = Vec3.atCenterOf(location.getPos());
			
			final Entity newEntity;
			
			// Teleport entity riding if there is one
			if (entity != null) {
				newEntity = LevelUtil.teleportEntity(entity, teleportLevel, teleportPos);
			} else {
				newEntity = null;
			}
			
			// Teleport minecart
			final Entity newCart = LevelUtil.teleportEntity(cart, teleportLevel, teleportPos);
			
			if (newEntity != null) {
				newEntity.startRiding(newCart, true);
				
				// For some reason the entity tracker does not update the passengers to the client so we send the packet manually.
				// See MC-U-Team/Useful-Railroads#21
				teleportLevel.getChunkSource().broadcastAndSend(newEntity, new ClientboundSetPassengersPacket(newCart));
			}
		}));
	}
	
	@Override
	public void saveNBT(CompoundTag compound) {
		compound.put("location", location.serializeNBT());
		if (fuel != 0) { // Don't save fuel if it's 0
			compound.putInt("fuel", fuel);
		}
	}
	
	@Override
	public void loadNBT(CompoundTag compound) {
		location.deserializeNBT(compound.getCompound("location"));
		fuel = compound.getInt("fuel");
	}
	
	@Override
	public void sendInitialMenuDataToClient(FriendlyByteBuf buffer) {
		checkCost();
		location.serialize(buffer);
		buffer.writeInt(fuel);
		buffer.writeInt(cost);
	}
	
	@Override
	public void handleInitialMenuDataFromServer(FriendlyByteBuf buffer) {
		location.deserialize(buffer);
		fuel = buffer.readInt();
		cost = buffer.readInt();
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction direction) {
		if (capability == ForgeCapabilities.ITEM_HANDLER && direction != Direction.UP) {
			return fuelSlotOptional.cast();
		}
		return super.getCapability(capability, direction);
	}
	
	@Override
	public void setRemoved() {
		fuelSlotOptional.invalidate();
		super.setRemoved();
	}
	
	@Override
	public void onChunkUnloaded() {
		fuelSlotOptional.invalidate();
		super.onChunkUnloaded();
	}
	
	public FuelItemHandler<TeleportRailFuelRecipe> getFuelSlot() {
		return fuelSlot;
	}
	
	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
		return new TeleportRailMenu(containerId, playerInventory, this);
	}
	
	@Override
	public Component getDisplayName() {
		return Component.translatable("container.usefulrailroads.teleport_rail");
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
