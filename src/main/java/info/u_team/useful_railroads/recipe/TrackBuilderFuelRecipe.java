package info.u_team.useful_railroads.recipe;

import info.u_team.useful_railroads.init.UsefulRailroadsRecipeSerializers;
import info.u_team.useful_railroads.init.UsefulRailroadsRecipeTypes;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class TrackBuilderFuelRecipe extends FuelRecipe {
	
	public TrackBuilderFuelRecipe(ResourceLocation id, String group, Ingredient ingredient, int fuel) {
		super(id, group, ingredient, fuel);
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return UsefulRailroadsRecipeSerializers.TRACK_BUILDER_FUEL.get();
	}
	
	@Override
	public IRecipeType<?> getType() {
		return UsefulRailroadsRecipeTypes.TRACK_BUILDER_FUEL;
	}
	
}
