package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.util.registry.CommonDeferredRegister;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.recipe.FuelRecipe;
import info.u_team.useful_railroads.recipe.TeleportRailFuelRecipe;
import info.u_team.useful_railroads.recipe.TeleportRailSpecialCraftingRecipe;
import info.u_team.useful_railroads.recipe.TrackBuilderFuelRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class UsefulRailroadsRecipeSerializers {
	
	public static final CommonDeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = CommonDeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, UsefulRailroadsMod.MODID);
	
	public static final RegistryObject<FuelRecipe.Serializer<TeleportRailFuelRecipe>> TELEPORT_RAIL_FUEL = RECIPE_SERIALIZERS.register("teleport_rail_fuel", () -> new FuelRecipe.Serializer<>(TeleportRailFuelRecipe::new));
	
	public static final RegistryObject<FuelRecipe.Serializer<TrackBuilderFuelRecipe>> TRACK_BUILDER_FUEL = RECIPE_SERIALIZERS.register("track_builder_fuel", () -> new FuelRecipe.Serializer<>(TrackBuilderFuelRecipe::new));
	
	public static final RegistryObject<SpecialRecipeSerializer<TeleportRailSpecialCraftingRecipe>> CRAFTING_SPECIAL_TELEPORT_RAIL_REMOVE_LOCATION = RECIPE_SERIALIZERS.register("crafting_special_teleport_rail_remove_location", () -> new SpecialRecipeSerializer<>(TeleportRailSpecialCraftingRecipe::new));
	
	public static void registerMod(IEventBus bus) {
		RECIPE_SERIALIZERS.register(bus);
	}
	
}
