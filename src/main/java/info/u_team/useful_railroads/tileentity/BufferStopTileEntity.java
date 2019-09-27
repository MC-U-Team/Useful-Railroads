package info.u_team.useful_railroads.tileentity;

import info.u_team.u_team_core.tileentity.UTileEntity;
import info.u_team.useful_railroads.init.UsefulRailroadsTileEntityTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.*;

public class BufferStopTileEntity extends UTileEntity {
	
	private final LazyOptional<ItemStackHandler> slots = LazyOptional.of(() -> new ItemStackHandler(10) {
		
		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			return getSlotLimit(slot);
		}
	});
	
	public BufferStopTileEntity() {
		super(UsefulRailroadsTileEntityTypes.BUFFER_STOP);
	}
	
	@Override
	public void writeNBT(CompoundNBT compound) {
		slots.ifPresent(handler -> handler.deserializeNBT(compound.getCompound("inventory")));
	}
	
	@Override
	public void readNBT(CompoundNBT compound) {
		slots.ifPresent(handler -> compound.put("inventory", handler.serializeNBT()));
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction direction) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && direction == Direction.DOWN || direction == null) {
			return slots.cast();
		}
		return super.getCapability(capability, direction);
	}
	
}
