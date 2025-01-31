package info.u_team.useful_railroads.recipe;

import info.u_team.useful_railroads.init.UsefulRailroadsBlocks;
import info.u_team.useful_railroads.init.UsefulRailroadsRecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class TeleportRailSpecialCraftingRecipe extends CustomRecipe {
	
	public TeleportRailSpecialCraftingRecipe(CraftingBookCategory category) {
		super(category);
	}
	
	@Override
	public boolean matches(CraftingInput input, Level level) {
		int count = 0;
		for (int i = 0; i < input.size(); i++) {
			final ItemStack stack = input.getItem(i);
			// TODO: maybe REWORK WITH CUSTOM COMPONENT
			final CustomData component = stack.get(DataComponents.BLOCK_ENTITY_DATA);
			final CompoundTag compound = component == null ? null : component.copyTag();
			if (stack.getItem() == UsefulRailroadsBlocks.TELEPORT_RAIL.getItem().asItem() && compound != null && compound.contains("location")) {
				count++;
			} else if (!stack.isEmpty()) {
				count = 0;
			}
		}
		return count == 1;
	}
	
	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		for (int i = 0; i < input.size(); i++) {
			final ItemStack stack = input.getItem(i);
			if (!stack.isEmpty()) {
				final ItemStack copy = stack.copy();
				final CustomData component = copy.get(DataComponents.BLOCK_ENTITY_DATA);
				final CompoundTag compound = component == null ? null : component.copyTag();
				compound.remove("location");
				compound.remove("id");
				if (compound.isEmpty()) {
					copy.remove(DataComponents.BLOCK_ENTITY_DATA);
				}
				return copy;
			}
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return UsefulRailroadsRecipeSerializers.CRAFTING_SPECIAL_TELEPORT_RAIL_REMOVE_LOCATION.get();
	}
	
}
