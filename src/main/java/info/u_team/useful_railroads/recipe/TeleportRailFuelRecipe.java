package info.u_team.useful_railroads.recipe;

import info.u_team.useful_railroads.init.UsefulRailroadsRecipeSerializers;
import info.u_team.useful_railroads.init.UsefulRailroadsRecipeTypes;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class TeleportRailFuelRecipe extends FuelRecipe {
	
	public TeleportRailFuelRecipe(ResourceLocation id, String group, Ingredient ingredient, int fuel) {
		super(id, group, ingredient, fuel);
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return UsefulRailroadsRecipeSerializers.TELEPORT_RAIL_FUEL.get();
	}
	
	@Override
	public IRecipeType<?> getType() {
		return UsefulRailroadsRecipeTypes.TELEPORT_RAIL_FUEL;
	}
	
}
