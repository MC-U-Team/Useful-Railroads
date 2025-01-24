package info.u_team.useful_railroads.recipe;

import info.u_team.useful_railroads.init.UsefulRailroadsRecipeSerializers;
import info.u_team.useful_railroads.init.UsefulRailroadsRecipeTypes;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class TrackBuilderFuelRecipe extends FuelRecipe {
	
	public TrackBuilderFuelRecipe(String group, Ingredient ingredient, int fuel) {
		super(group, ingredient, fuel);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return UsefulRailroadsRecipeSerializers.TRACK_BUILDER_FUEL.get();
	}
	
	@Override
	public RecipeType<?> getType() {
		return UsefulRailroadsRecipeTypes.TRACK_BUILDER_FUEL.get();
	}
	
}
