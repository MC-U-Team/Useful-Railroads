package info.u_team.useful_railroads.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;

public class InvWrapperOnlyInsert extends InvWrapper {
	
	public InvWrapperOnlyInsert(IInventory inv) {
		super(inv);
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}
	
}
