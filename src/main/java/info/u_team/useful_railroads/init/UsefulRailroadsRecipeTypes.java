package info.u_team.useful_railroads.init;

import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.recipe.*;
import net.minecraft.item.crafting.IRecipeType;

public class UsefulRailroadsRecipeTypes {
	
	public static IRecipeType<TeleportRailFuelRecipe> TELEPORT_RAIL_FUEL = IRecipeType.register(UsefulRailroadsMod.MODID + ":teleport_rail_fuel");
	
	public static IRecipeType<TrackBuilderFuelRecipe> TRACK_BUILDER_FUEL = IRecipeType.register(UsefulRailroadsMod.MODID + ":track_builder_fuel");
	
}
