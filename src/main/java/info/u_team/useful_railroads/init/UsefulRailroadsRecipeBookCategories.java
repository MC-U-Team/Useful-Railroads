package info.u_team.useful_railroads.init;

import net.minecraft.client.RecipeBookCategories;
import net.minecraftforge.client.RecipeBookRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class UsefulRailroadsRecipeBookCategories {
	
	private static void setup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			RecipeBookCategories.UNKNOWN.getIconItems(); // Force initialization of RecipeBookCategories
			
			RecipeBookRegistry.addCategoriesFinder(UsefulRailroadsRecipeTypes.TELEPORT_RAIL_FUEL.get(), recipe -> RecipeBookCategories.UNKNOWN);
			RecipeBookRegistry.addCategoriesFinder(UsefulRailroadsRecipeTypes.TRACK_BUILDER_FUEL.get(), recipe -> RecipeBookCategories.UNKNOWN);
		});
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(UsefulRailroadsRecipeBookCategories::setup);
	}
	
}
