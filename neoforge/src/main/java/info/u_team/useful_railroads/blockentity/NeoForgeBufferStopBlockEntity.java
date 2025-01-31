package info.u_team.useful_railroads.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class NeoForgeBufferStopBlockEntity extends BufferStopBlockEntity {
	
	private final IItemHandlerModifiable minecartSlotsWrapper = new InvWrapper(getMinecartSlots());
	
	public NeoForgeBufferStopBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}
	
	public IItemHandlerModifiable getMinecartSlotsWrapper() {
		return minecartSlotsWrapper;
	}
	
	public static class Factory implements BufferStopBlockEntity.Factory {
		
		@Override
		public BufferStopBlockEntity create(BlockPos pos, BlockState state) {
			return new NeoForgeBufferStopBlockEntity(pos, state);
		}
	}
	
}
