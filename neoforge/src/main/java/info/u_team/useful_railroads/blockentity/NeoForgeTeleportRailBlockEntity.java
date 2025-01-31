package info.u_team.useful_railroads.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class NeoForgeTeleportRailBlockEntity extends TeleportRailBlockEntity {
	
	private final IItemHandlerModifiable fuelSlotWrapper = new InvWrapper(getFuelSlot());
	
	public NeoForgeTeleportRailBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}
	
	public IItemHandlerModifiable getFuelSlotWrapper() {
		return fuelSlotWrapper;
	}
	
	public static class Factory implements TeleportRailBlockEntity.Factory {
		
		@Override
		public TeleportRailBlockEntity create(BlockPos pos, BlockState state) {
			return new NeoForgeTeleportRailBlockEntity(pos, state);
		}
	}
	
}
