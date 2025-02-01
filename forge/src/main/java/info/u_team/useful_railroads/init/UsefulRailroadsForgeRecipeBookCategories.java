package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.util.registry.BusRegister;
import net.minecraft.client.RecipeBookCategories;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;

public class UsefulRailroadsForgeRecipeBookCategories {
	
	private static void registerCategory(RegisterRecipeBookCategoriesEvent event) {
		event.registerRecipeCategoryFinder(UsefulRailroadsRecipeTypes.TELEPORT_RAIL_FUEL.get(), recipe -> RecipeBookCategories.UNKNOWN);
		event.registerRecipeCategoryFinder(UsefulRailroadsRecipeTypes.TRACK_BUILDER_FUEL.get(), recipe -> RecipeBookCategories.UNKNOWN);
	}
	
	static void register() {
		BusRegister.registerMod(bus -> bus.addListener(UsefulRailroadsForgeRecipeBookCategories::registerCategory));
	}
	
}
