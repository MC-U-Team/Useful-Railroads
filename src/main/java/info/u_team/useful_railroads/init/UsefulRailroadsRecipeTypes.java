package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.util.registry.CommonDeferredRegister;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.recipe.TeleportRailFuelRecipe;
import info.u_team.useful_railroads.recipe.TrackBuilderFuelRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class UsefulRailroadsRecipeTypes {
	
	public static final CommonDeferredRegister<RecipeType<?>> RECIPE_TYPES = CommonDeferredRegister.create(ForgeRegistries.RECIPE_TYPES, UsefulRailroadsMod.MODID);
	
	public static final RegistryObject<RecipeType<TeleportRailFuelRecipe>> TELEPORT_RAIL_FUEL = RECIPE_TYPES.register("teleport_rail_fuel", RecipeType::simple);
	public static final RegistryObject<RecipeType<TrackBuilderFuelRecipe>> TRACK_BUILDER_FUEL = RECIPE_TYPES.register("track_builder_fuel", RecipeType::simple);
	
	public static void registerMod(IEventBus bus) {
		RECIPE_TYPES.register(bus);
	}
	
}
