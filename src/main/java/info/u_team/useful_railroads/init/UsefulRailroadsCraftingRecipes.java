package info.u_team.useful_railroads.init;

import info.u_team.useful_railroads.UsefulRailroadsConstants;
import info.u_team.useful_railroads.recipe.RecipeTeleportRail;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class UsefulRailroadsCraftingRecipes {
	
	public static void init() {
		register(new RecipeTeleportRail(), "teleport_rail_remove_nbt");
	}
	
	private static void register(IRecipe recipe, String name) {
		recipe.setRegistryName(new ResourceLocation(UsefulRailroadsConstants.MODID, name));
		ForgeRegistries.RECIPES.register(recipe);
	}
	
}
