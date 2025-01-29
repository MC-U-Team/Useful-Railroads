package info.u_team.useful_railroads.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ForgeBufferStopBlockEntity extends BufferStopBlockEntity {
	
	private final LazyOptional<IItemHandlerModifiable> minecartSlotsOptional = LazyOptional.of(() -> new InvWrapper(getMinecartSlots()));
	
	public ForgeBufferStopBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction direction) {
		if (capability == ForgeCapabilities.ITEM_HANDLER && (direction == Direction.DOWN || direction == null)) {
			return minecartSlotsOptional.cast();
		}
		return super.getCapability(capability, direction);
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
	
	public static class Factory implements BufferStopBlockEntity.Factory {
		
		@Override
		public BufferStopBlockEntity create(BlockPos pos, BlockState state) {
			return new ForgeBufferStopBlockEntity(pos, state);
		}
	}
	
}
