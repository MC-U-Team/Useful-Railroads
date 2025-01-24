package info.u_team.useful_railroads.init;

import net.minecraft.client.RecipeBookCategories;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class UsefulRailroadsRecipeBookCategories {
	
	private static void register(RegisterRecipeBookCategoriesEvent event) {
		event.registerRecipeCategoryFinder(UsefulRailroadsRecipeTypes.TELEPORT_RAIL_FUEL.get(), recipe -> RecipeBookCategories.UNKNOWN);
		event.registerRecipeCategoryFinder(UsefulRailroadsRecipeTypes.TRACK_BUILDER_FUEL.get(), recipe -> RecipeBookCategories.UNKNOWN);
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(UsefulRailroadsRecipeBookCategories::register);
	}
	
}
