package info.u_team.useful_railroads.data.builder;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import info.u_team.useful_railroads.init.UsefulRailroadsRecipeSerializers;
import net.minecraft.advancements.*;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.*;
import net.minecraft.util.ResourceLocation;

public class FuelRecipeBuilder {
	
	private final Ingredient ingredient;
	private final int fuel;
	private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();
	private String group;
	
	public FuelRecipeBuilder(Ingredient ingredient, int fuel) {
		this.ingredient = ingredient;
		this.fuel = fuel;
	}
	
	public FuelRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn) {
		this.advancementBuilder.withCriterion(name, criterionIn);
		return this;
	}
	
	public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
		this.build(consumerIn, new ResourceLocation(save));
	}
	
	public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
		this.validate(id);
		this.advancementBuilder.withParentId(new ResourceLocation("recipes/root")).withCriterion("has_the_recipe", new RecipeUnlockedTrigger.Instance(id)).withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
		consumerIn.accept(new FuelRecipeBuilder.Result(id, group == null ? "" : group, ingredient, fuel, advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath())));
	}
	
	private void validate(ResourceLocation id) {
		if (this.advancementBuilder.getCriteria().isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + id);
		}
	}
	
	public static class Result implements IFinishedRecipe {
		
		private final ResourceLocation id;
		private final String group;
		private final Ingredient ingredient;
		private final int fuel;
		private final Advancement.Builder advancementBuilder;
		private final ResourceLocation advancementId;
		
		public Result(ResourceLocation id, String group, Ingredient ingredient, int fuel, Advancement.Builder advancementBuilder, ResourceLocation advancementId) {
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
			return UsefulRailroadsRecipeSerializers.TELEPORT_FUEL;
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