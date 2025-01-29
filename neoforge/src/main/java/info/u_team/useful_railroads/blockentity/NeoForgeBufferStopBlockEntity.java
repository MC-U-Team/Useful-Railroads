package info.u_team.useful_railroads.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class NeoForgeBufferStopBlockEntity extends BufferStopBlockEntity {
	
	public NeoForgeBufferStopBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}
	
	public static class Factory implements BufferStopBlockEntity.Factory {
		
		@Override
		public BufferStopBlockEntity create(BlockPos pos, BlockState state) {
			return new NeoForgeBufferStopBlockEntity(pos, state);
		}
	}
	
}
