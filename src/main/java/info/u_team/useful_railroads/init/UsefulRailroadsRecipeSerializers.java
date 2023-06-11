package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.registry.CommonRegister;
import info.u_team.u_team_core.api.registry.RegistryEntry;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.recipe.FuelRecipe;
import info.u_team.useful_railroads.recipe.TeleportRailFuelRecipe;
import info.u_team.useful_railroads.recipe.TeleportRailSpecialCraftingRecipe;
import info.u_team.useful_railroads.recipe.TrackBuilderFuelRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class UsefulRailroadsRecipeSerializers {
	
	public static final CommonRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = CommonRegister.create(Registries.RECIPE_SERIALIZER, UsefulRailroadsMod.MODID);
	
	public static final RegistryEntry<FuelRecipe.Serializer<TeleportRailFuelRecipe>> TELEPORT_RAIL_FUEL = RECIPE_SERIALIZERS.register("teleport_rail_fuel", () -> new FuelRecipe.Serializer<>(TeleportRailFuelRecipe::new));
	public static final RegistryEntry<FuelRecipe.Serializer<TrackBuilderFuelRecipe>> TRACK_BUILDER_FUEL = RECIPE_SERIALIZERS.register("track_builder_fuel", () -> new FuelRecipe.Serializer<>(TrackBuilderFuelRecipe::new));
	
	public static final RegistryEntry<SimpleCraftingRecipeSerializer<TeleportRailSpecialCraftingRecipe>> CRAFTING_SPECIAL_TELEPORT_RAIL_REMOVE_LOCATION = RECIPE_SERIALIZERS.register("crafting_special_teleport_rail_remove_location", () -> new SimpleCraftingRecipeSerializer<>(TeleportRailSpecialCraftingRecipe::new));
	
	static void register() {
		RECIPE_SERIALIZERS.register();
	}
	
}
