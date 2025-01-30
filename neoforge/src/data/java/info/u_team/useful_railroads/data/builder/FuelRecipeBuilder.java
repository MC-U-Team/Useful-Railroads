package info.u_team.useful_railroads.data.builder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import info.u_team.useful_railroads.recipe.FuelRecipe;
import info.u_team.useful_railroads.recipe.TeleportRailFuelRecipe;
import info.u_team.useful_railroads.recipe.TrackBuilderFuelRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class FuelRecipeBuilder implements RecipeBuilder {
	
	private final FuelRecipe.Serializer.Factory<?> factory;
	
	private final Ingredient ingredient;
	private final int fuel;
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
	private String group;
	
	public static FuelRecipeBuilder teleportRailFuel(Ingredient ingredient, int fuel) {
		return new FuelRecipeBuilder(TeleportRailFuelRecipe::new, ingredient, fuel);
	}
	
	public static FuelRecipeBuilder trackBuilderFuel(Ingredient ingredient, int fuel) {
		return new FuelRecipeBuilder(TrackBuilderFuelRecipe::new, ingredient, fuel);
	}
	
	protected FuelRecipeBuilder(FuelRecipe.Serializer.Factory<?> factory, Ingredient ingredient, int fuel) {
		this.factory = factory;
		this.ingredient = ingredient;
		this.fuel = fuel;
	}
	
	@Override
	public FuelRecipeBuilder unlockedBy(String key, Criterion<?> criterion) {
		criteria.put(key, criterion);
		return this;
	}
	
	@Override
	public FuelRecipeBuilder group(String group) {
		this.group = group;
		return this;
	}
	
	@Override
	public Item getResult() {
		return Items.AIR;
	}
	
	@Override
	public void save(RecipeOutput recipeOutput) {
		throw new IllegalStateException("Id must be specified, use other method with resource location");
	}
	
	@Override
	public void save(RecipeOutput output, ResourceLocation id) {
		validate(id);
		final Advancement.Builder builder = output.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
		criteria.forEach(builder::addCriterion);
		final FuelRecipe fuelRecipe = factory.create(Objects.requireNonNullElse(group, ""), ingredient, fuel);
		output.accept(id, fuelRecipe, builder.build(id.withPrefix("recipes/")));
	}
	
	private void validate(ResourceLocation id) {
		if (criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + id);
		}
	}
	
}