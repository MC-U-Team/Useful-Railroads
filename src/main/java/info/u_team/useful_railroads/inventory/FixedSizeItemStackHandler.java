package info.u_team.useful_railroads.inventory;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.nbt.CompoundNBT;
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
	public CompoundNBT serializeNBT() {
		final CompoundNBT compound = new CompoundNBT();
		ItemStackHelper.saveAllItems(compound, stacks, false);
		return compound;
	}
	
	@Override
	public void deserializeNBT(CompoundNBT compound) {
		ItemStackHelper.loadAllItems(compound, stacks);
		onLoad();
	}
}
