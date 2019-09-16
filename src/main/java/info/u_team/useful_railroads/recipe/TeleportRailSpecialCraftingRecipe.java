package info.u_team.useful_railroads.recipe;

import info.u_team.useful_railroads.init.*;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TeleportRailSpecialCraftingRecipe extends SpecialRecipe {
	
	public TeleportRailSpecialCraftingRecipe(ResourceLocation id) {
		super(id);
	}
	
	@Override
	public boolean matches(CraftingInventory inventory, World world) {
		int count = 0;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			final ItemStack stack = inventory.getStackInSlot(i);
			final CompoundNBT compound = stack.getChildTag("BlockEntityTag");
			if (stack.getItem() == UsefulRailroadsBlocks.TELEPORT_RAIL.asItem() && compound != null && compound.contains("location")) {
				count++;
			} else if (!stack.isEmpty()) {
				count = 0;
			}
		}
		return count == 1;
	}
	
	@Override
	public ItemStack getCraftingResult(CraftingInventory inventory) {
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			final ItemStack stack = inventory.getStackInSlot(i);
			if (!stack.isEmpty()) {
				final ItemStack copy = stack.copy();
				final CompoundNBT compound = copy.getChildTag("BlockEntityTag");
				compound.remove("location");
				if (compound.isEmpty()) {
					copy.removeChildTag("BlockEntityTag");
				}
				return copy;
			}
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return UsefulRailroadsRecipeSerializers.CRAFTING_SPECIAL_TELEPORT_RAIL;
	}
	
}
