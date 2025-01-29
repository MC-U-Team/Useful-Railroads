package info.u_team.useful_railroads.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ForgeTeleportRailBlockEntity extends TeleportRailBlockEntity {
	
	private final LazyOptional<IItemHandlerModifiable> fuelSlotOptional = LazyOptional.of(() -> new InvWrapper(getFuelSlot()));
	
	public ForgeTeleportRailBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
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
	
	public static class Factory implements TeleportRailBlockEntity.Factory {
		
		@Override
		public TeleportRailBlockEntity create(BlockPos pos, BlockState state) {
			return new ForgeTeleportRailBlockEntity(pos, state);
		}
	}
	
}
