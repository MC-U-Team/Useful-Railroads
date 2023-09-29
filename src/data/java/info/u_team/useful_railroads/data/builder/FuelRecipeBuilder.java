package info.u_team.useful_railroads.data.builder;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import info.u_team.useful_railroads.init.UsefulRailroadsRecipeSerializers;
import info.u_team.useful_railroads.recipe.FuelRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class FuelRecipeBuilder implements RecipeBuilder {
	
	private final FuelRecipe.Serializer<?> serializer;
	
	private final Ingredient ingredient;
	private final int fuel;
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
	private String group;
	
	public static FuelRecipeBuilder teleportRailFuel(Ingredient ingredient, int fuel) {
		return new FuelRecipeBuilder(UsefulRailroadsRecipeSerializers.TELEPORT_RAIL_FUEL.get(), ingredient, fuel);
	}
	
	public static FuelRecipeBuilder trackBuilderFuel(Ingredient ingredient, int fuel) {
		return new FuelRecipeBuilder(UsefulRailroadsRecipeSerializers.TRACK_BUILDER_FUEL.get(), ingredient, fuel);
	}
	
	protected FuelRecipeBuilder(FuelRecipe.Serializer<?> serializer, Ingredient ingredient, int fuel) {
		this.serializer = serializer;
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
	public void save(RecipeOutput output, ResourceLocation id) {
		validate(id);
		final Advancement.Builder builder = output.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
		criteria.forEach(builder::addCriterion);
		output.accept(new FuelRecipeBuilder.Result(serializer, id, group == null ? "" : group, ingredient, fuel, builder.build(id.withPrefix("recipes/"))));
	}
	
	private void validate(ResourceLocation id) {
		if (criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + id);
		}
	}
	
	public static record Result(FuelRecipe.Serializer<?> serializer, ResourceLocation id, String group, Ingredient ingredient, int fuel, AdvancementHolder advancement) implements FinishedRecipe {
		
		@Override
		public void serializeRecipeData(JsonObject json) {
			if (!group.isEmpty()) {
				json.addProperty("group", group);
			}
			
			json.add("ingredient", ingredient.toJson(false));
			json.addProperty("fuel", fuel);
		}
		
		@Override
		public RecipeSerializer<?> type() {
			return serializer;
		}
		
		@Override
		public ResourceLocation id() {
			return id;
		}
		
		@Override
		public AdvancementHolder advancement() {
			return advancement;
		}
	}
	
}