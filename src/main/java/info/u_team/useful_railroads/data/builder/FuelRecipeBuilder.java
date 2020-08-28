package info.u_team.useful_railroads.data.builder;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import info.u_team.useful_railroads.init.UsefulRailroadsRecipeSerializers;
import info.u_team.useful_railroads.recipe.FuelRecipe;
import net.minecraft.advancements.*;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.*;
import net.minecraft.util.ResourceLocation;

public class FuelRecipeBuilder {
	
	private final FuelRecipe.Serializer<?> serializer;
	
	private final Ingredient ingredient;
	private final int fuel;
	private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();
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
	
	public FuelRecipeBuilder addCriterion(String name, ICriterionInstance criterion) {
		advancementBuilder.withCriterion(name, criterion);
		return this;
	}
	
	public void build(Consumer<IFinishedRecipe> consumer, String save) {
		build(consumer, new ResourceLocation(save));
	}
	
	public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
		validate(id);
		advancementBuilder.withParentId(new ResourceLocation("recipes/root")).withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id)).withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
		consumer.accept(new FuelRecipeBuilder.Result(serializer, id, group == null ? "" : group, ingredient, fuel, advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath())));
	}
	
	private void validate(ResourceLocation id) {
		if (advancementBuilder.getCriteria().isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + id);
		}
	}
	
	public static class Result implements IFinishedRecipe {
		
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
		
		public void serialize(JsonObject json) {
			if (!this.group.isEmpty()) {
				json.addProperty("group", group);
			}
			
			json.add("ingredient", ingredient.serialize());
			json.addProperty("fuel", fuel);
		}
		
		public IRecipeSerializer<?> getSerializer() {
			return serializer;
		}
		
		public ResourceLocation getID() {
			return id;
		}
		
		@Nullable
		public JsonObject getAdvancementJson() {
			return advancementBuilder.serialize();
		}
		
		@Nullable
		public ResourceLocation getAdvancementID() {
			return advancementId;
		}
	}
}