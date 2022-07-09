package info.u_team.useful_railroads.blockentity;

import info.u_team.u_team_core.blockentity.UBlockEntity;
import info.u_team.u_team_core.inventory.TileEntityUItemStackHandler;
import info.u_team.u_team_core.inventory.UItemStackHandler;
import info.u_team.useful_railroads.init.UsefulRailroadsBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class BufferStopBlockEntity extends UBlockEntity {
	
	private final UItemStackHandler minecartSlots = new TileEntityUItemStackHandler(10, this) {
		
		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			return getSlotLimit(slot);
		}
	};
	
	private final LazyOptional<UItemStackHandler> minecartSlotsOptional = LazyOptional.of(() -> minecartSlots);
	
	public BufferStopBlockEntity(BlockPos pos, BlockState state) {
		super(UsefulRailroadsBlockEntityTypes.BUFFER_STOP.get(), pos, state);
	}
	
	@Override
	public void saveNBT(CompoundTag compound) {
		minecartSlots.deserializeNBT(compound.getCompound("inventory"));
	}
	
	@Override
	public void loadNBT(CompoundTag compound) {
		compound.put("inventory", minecartSlots.serializeNBT());
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction direction) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && (direction == Direction.DOWN || direction == null)) {
			return minecartSlotsOptional.cast();
		}
		return super.getCapability(capability, direction);
	}
	
	public UItemStackHandler getMinecartSlots() {
		return minecartSlots;
	}
	
	@Override
	public void setRemoved() {
		super.setRemoved();
		minecartSlotsOptional.invalidate();
	}
	
}
