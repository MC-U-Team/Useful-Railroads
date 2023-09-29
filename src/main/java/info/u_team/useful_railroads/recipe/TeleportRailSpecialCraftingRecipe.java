package info.u_team.useful_railroads.recipe;

import info.u_team.useful_railroads.init.UsefulRailroadsBlocks;
import info.u_team.useful_railroads.init.UsefulRailroadsRecipeSerializers;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class TeleportRailSpecialCraftingRecipe extends CustomRecipe {
	
	public TeleportRailSpecialCraftingRecipe(CraftingBookCategory category) {
		super(category);
	}
	
	@Override
	public boolean matches(CraftingContainer inventory, Level level) {
		int count = 0;
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			final ItemStack stack = inventory.getItem(i);
			final CompoundTag compound = stack.getTagElement(BlockItem.BLOCK_ENTITY_TAG);
			if (stack.getItem() == UsefulRailroadsBlocks.TELEPORT_RAIL.getItem().asItem() && compound != null && compound.contains("location")) {
				count++;
			} else if (!stack.isEmpty()) {
				count = 0;
			}
		}
		return count == 1;
	}
	
	@Override
	public ItemStack assemble(CraftingContainer inventory, RegistryAccess registryAccess) {
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			final ItemStack stack = inventory.getItem(i);
			if (!stack.isEmpty()) {
				final ItemStack copy = stack.copy();
				final CompoundTag compound = copy.getTagElement(BlockItem.BLOCK_ENTITY_TAG);
				compound.remove("location");
				if (compound.isEmpty()) {
					copy.removeTagKey(BlockItem.BLOCK_ENTITY_TAG);
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
