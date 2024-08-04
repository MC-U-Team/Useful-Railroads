package info.u_team.useful_railroads.inventory;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class FixedSizeItemStackHandler extends ItemStackHandler {
	
	public FixedSizeItemStackHandler(int size) {
		super(size);
	}
	
	@Override
	public void setSize(int size) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider registries) {
		final CompoundTag compound = new CompoundTag();
		ContainerHelper.saveAllItems(compound, stacks, false, registries);
		return compound;
	}
	
	@Override
	public void deserializeNBT(HolderLookup.Provider registries, CompoundTag compound) {
		ContainerHelper.loadAllItems(compound, stacks, registries);
		onLoad();
	}
	
	public NonNullList<ItemStack> getItems() {
		return stacks;
	}
}
