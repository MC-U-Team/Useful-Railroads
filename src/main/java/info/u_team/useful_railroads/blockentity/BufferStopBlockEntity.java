package info.u_team.useful_railroads.blockentity;

import info.u_team.u_team_core.blockentity.UBlockEntity;
import info.u_team.u_team_core.inventory.BlockEntityUItemStackHandler;
import info.u_team.u_team_core.inventory.UItemStackHandler;
import info.u_team.useful_railroads.init.UsefulRailroadsBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

public class BufferStopBlockEntity extends UBlockEntity {
	
	private final UItemStackHandler minecartSlots = new BlockEntityUItemStackHandler(10, this) {
		
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
		compound.put("inventory", minecartSlots.serializeNBT());
	}
	
	@Override
	public void loadNBT(CompoundTag compound) {
		minecartSlots.deserializeNBT(compound.getCompound("inventory"));
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction direction) {
		if (capability == ForgeCapabilities.ITEM_HANDLER && (direction == Direction.DOWN || direction == null)) {
			return minecartSlotsOptional.cast();
		}
		return super.getCapability(capability, direction);
	}
	
	public UItemStackHandler getMinecartSlots() {
		return minecartSlots;
	}
	
	@Override
	public void setRemoved() {
		minecartSlotsOptional.invalidate();
		super.setRemoved();
	}
	
	@Override
	public void onChunkUnloaded() {
		minecartSlotsOptional.invalidate();
		super.onChunkUnloaded();
	}
	
}
