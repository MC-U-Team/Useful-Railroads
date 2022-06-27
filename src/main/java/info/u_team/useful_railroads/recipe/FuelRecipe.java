package info.u_team.useful_railroads.recipe;

import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public abstract class FuelRecipe implements Recipe<Container> {
	
	protected final Ingredient ingredient;
	protected final int fuel;
	protected final ResourceLocation id;
	protected final String group;
	
	public FuelRecipe(ResourceLocation id, String group, Ingredient ingredient, int fuel) {
		this.id = id;
		this.group = group;
		this.ingredient = ingredient;
		this.fuel = fuel;
	}
	
	@Override
	public ResourceLocation getId() {
		return id;
	}
	
	@Override
	public String getGroup() {
		return group;
	}
	
	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		final NonNullList<Ingredient> ingredients = NonNullList.create();
		ingredients.add(ingredient);
		return ingredients;
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack assemble(Container inv) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean matches(Container inv, Level level) {
		return ingredient.test(inv.getItem(0));
	}
	
	public int getFuel() {
		return fuel;
	}
	
	public static class Serializer<T extends FuelRecipe> implements RecipeSerializer<T> {
		
		private final IFactory<T> factory;
		
		public Serializer(IFactory<T> factory) {
			this.factory = factory;
		}
		
		@Override
		public T fromJson(ResourceLocation id, JsonObject json) {
			final String group = GsonHelper.getAsString(json, "group", "");
			final Ingredient ingredient;
			if (GsonHelper.isArrayNode(json, "ingredient")) {
				ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredient"));
			} else {
				ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
			}
			final int fuel = GsonHelper.getAsInt(json, "fuel");
			return factory.create(id, group, ingredient, fuel);
		}
		
		@Override
		public T fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
			final String group = buffer.readUtf(32767);
			final Ingredient ingredient = Ingredient.fromNetwork(buffer);
			final int fuel = buffer.readInt();
			return factory.create(id, group, ingredient, fuel);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, T recipe) {
			buffer.writeUtf(recipe.group);
			recipe.ingredient.toNetwork(buffer);
			buffer.writeInt(recipe.fuel);
		}
		
		public static interface IFactory<T extends FuelRecipe> {
			
			T create(ResourceLocation id, String group, Ingredient ingredient, int fuel);
		}
	}
}
