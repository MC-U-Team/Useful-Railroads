package info.u_team.useful_railroads.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class NeoForgeTeleportRailBlockEntity extends TeleportRailBlockEntity {
	
	public NeoForgeTeleportRailBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}
	
	public static class Factory implements TeleportRailBlockEntity.Factory {
		
		@Override
		public TeleportRailBlockEntity create(BlockPos pos, BlockState state) {
			return new NeoForgeTeleportRailBlockEntity(pos, state);
		}
	}
	
}
