package info.u_team.useful_railroads.data.builder;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import info.u_team.useful_railroads.init.UsefulRailroadsRecipeSerializers;
import info.u_team.useful_railroads.recipe.FuelRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class FuelRecipeBuilder {
	
	private final FuelRecipe.Serializer<?> serializer;
	
	private final Ingredient ingredient;
	private final int fuel;
	private final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();
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
	
	public FuelRecipeBuilder addCriterion(String name, CriterionTriggerInstance criterion) {
		advancementBuilder.addCriterion(name, criterion);
		return this;
	}
	
	public void save(Consumer<FinishedRecipe> consumer, String save) {
		save(consumer, new ResourceLocation(save));
	}
	
	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		validate(id);
		advancementBuilder.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
		consumer.accept(new FuelRecipeBuilder.Result(serializer, id, group == null ? "" : group, ingredient, fuel, advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath())));
	}
	
	private void validate(ResourceLocation id) {
		if (advancementBuilder.getCriteria().isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + id);
		}
	}
	
	public static class Result implements FinishedRecipe {
		
		private final FuelRecipe.Serializer<?> serializer;
		
		private final ResourceLocation id;
		private final String group;
		private final Ingredient ingredient;
		private final int fuel;
		private final Advancement.Builder advancementBuilder;
		private final ResourceLocation advancementId;
		
		public Result(FuelRecipe.Serializer<?> serializer, ResourceLocation id, String group, Ingredient ingredient, int fuel, Advancement.Builder advancementBuilder, ResourceLocation advancementId) {
			this.serializer = serializer;
			this.id = id;
			this.group = group;
			this.ingredient = ingredient;
			this.fuel = fuel;
			this.advancementBuilder = advancementBuilder;
			this.advancementId = advancementId;
		}
		
		@Override
		public void serializeRecipeData(JsonObject json) {
			if (!this.group.isEmpty()) {
				json.addProperty("group", group);
			}
			
			json.add("ingredient", ingredient.toJson());
			json.addProperty("fuel", fuel);
		}
		
		@Override
		public RecipeSerializer<?> getType() {
			return serializer;
		}
		
		@Override
		public ResourceLocation getId() {
			return id;
		}
		
		@Override
		@Nullable
		public JsonObject serializeAdvancement() {
			return advancementBuilder.serializeToJson();
		}
		
		@Override
		@Nullable
		public ResourceLocation getAdvancementId() {
			return advancementId;
		}
	}
}