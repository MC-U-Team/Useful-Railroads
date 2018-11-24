package info.u_team.useful_railroads.recipe;

import info.u_team.useful_railroads.init.UsefulRailroadsBlocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeTeleportRail extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	
	@Override
	public boolean matches(InventoryCrafting inventory, World world) {
		int count = 0;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() == UsefulRailroadsBlocks.rail_teleport.getItem() && stack.hasTagCompound() && stack.getTagCompound().hasKey("dim")) {
				count++;
			} else if (!stack.isEmpty()) {
				count = 0;
			}
		}
		System.out.println(count);
		return count == 1;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return getRecipeOutput();
	}
	
	@Override
	public boolean canFit(int width, int height) {
		return width > 0 && height > 0;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(UsefulRailroadsBlocks.rail_teleport);
	}
	
}
