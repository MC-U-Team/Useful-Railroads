package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.registry.CommonRegister;
import info.u_team.u_team_core.api.registry.RegistryEntry;
import info.u_team.useful_railroads.UsefulRailroadsReference;
import info.u_team.useful_railroads.recipe.TeleportRailFuelRecipe;
import info.u_team.useful_railroads.recipe.TrackBuilderFuelRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;

public class UsefulRailroadsRecipeTypes {
	
	public static final CommonRegister<RecipeType<?>> RECIPE_TYPES = CommonRegister.create(Registries.RECIPE_TYPE, UsefulRailroadsReference.MODID);
	
	public static final RegistryEntry<RecipeType<TeleportRailFuelRecipe>> TELEPORT_RAIL_FUEL = RECIPE_TYPES.register("teleport_rail_fuel", name -> new RecipeType<TeleportRailFuelRecipe>() {
		
		// TODO move maybe common recipe type creator to uteamcore?
		@Override
		public String toString() {
			return name.toString();
		}
	});
	public static final RegistryEntry<RecipeType<TrackBuilderFuelRecipe>> TRACK_BUILDER_FUEL = RECIPE_TYPES.register("track_builder_fuel", name -> new RecipeType<TrackBuilderFuelRecipe>() {
		
		// TODO move maybe common recipe type creator to uteamcore?
		@Override
		public String toString() {
			return name.toString();
		}
	});
	
	static void register() {
		RECIPE_TYPES.register();
	}
	
}
