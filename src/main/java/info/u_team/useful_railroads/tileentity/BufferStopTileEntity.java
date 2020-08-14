package info.u_team.useful_railroads.tileentity;

import info.u_team.u_team_core.inventory.*;
import info.u_team.u_team_core.tileentity.UTileEntity;
import info.u_team.useful_railroads.init.UsefulRailroadsTileEntityTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class BufferStopTileEntity extends UTileEntity {
	
	private final UItemStackHandler minecartSlots = new TileEntityUItemStackHandler(10, this) {
		
		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			return getSlotLimit(slot);
		}
	};
	
	private final LazyOptional<UItemStackHandler> minecartSlotsOptional = LazyOptional.of(() -> minecartSlots);
	
	public BufferStopTileEntity() {
		super(UsefulRailroadsTileEntityTypes.BUFFER_STOP.get());
	}
	
	@Override
	public void writeNBT(CompoundNBT compound) {
		minecartSlots.deserializeNBT(compound.getCompound("inventory"));
	}
	
	@Override
	public void readNBT(CompoundNBT compound) {
		compound.put("inventory", minecartSlots.serializeNBT());
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction direction) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && (direction == Direction.DOWN || direction == null)) {
			return minecartSlotsOptional.cast();
		}
		return super.getCapability(capability, direction);
	}
	
	@Override
	public void remove() {
		super.remove();
		minecartSlotsOptional.invalidate();
	}
	
}
