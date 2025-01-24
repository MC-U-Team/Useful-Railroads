package info.u_team.useful_railroads.blockentity;

import info.u_team.u_team_core.blockentity.UBlockEntity;
import info.u_team.u_team_core.inventory.BlockEntityUItemStackContainer;
import info.u_team.u_team_core.inventory.UItemStackContainer;
import info.u_team.useful_railroads.init.UsefulRailroadsBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BufferStopBlockEntity extends UBlockEntity {
	
	private final UItemStackContainer minecartSlots = new BlockEntityUItemStackContainer(10, this) {
		
		public int getMaxStackSize(ItemStack stack) {
			return getMaxStackSize();
		};
	};
	
	public BufferStopBlockEntity(BlockPos pos, BlockState state) {
		super(UsefulRailroadsBlockEntityTypes.BUFFER_STOP.get(), pos, state);
	}
	
	@Override
	public void saveNBT(CompoundTag compound, HolderLookup.Provider registries) {
		compound.put("inventory", minecartSlots.serializeNBT(registries));
	}
	
	@Override
	public void loadNBT(CompoundTag compound, HolderLookup.Provider registries) {
		minecartSlots.deserializeNBT(compound.getCompound("inventory"), registries);
	}
	
	public UItemStackContainer getMinecartSlots() {
		return minecartSlots;
	}
	
}
