package info.u_team.useful_railroads.recipe;

import com.google.gson.JsonObject;

import info.u_team.u_team_core.recipeserializer.URecipeSerializer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.*;
import net.minecraft.world.World;

public abstract class FuelRecipe implements IRecipe<IInventory> {
	
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
	
	public ResourceLocation getId() {
		return id;
	}
	
	public String getGroup() {
		return group;
	}
	
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
	
	public NonNullList<Ingredient> getIngredients() {
		final NonNullList<Ingredient> ingredients = NonNullList.create();
		ingredients.add(ingredient);
		return ingredients;
	}
	
	public boolean canFit(int width, int height) {
		return true;
	}
	
	public ItemStack getCraftingResult(IInventory inv) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean matches(IInventory inv, World world) {
		return ingredient.test(inv.getStackInSlot(0));
	}
	
	public int getFuel() {
		return fuel;
	}
	
	public static class Serializer<T extends FuelRecipe> extends URecipeSerializer<T> {
		
		private final IFactory<T> factory;
		
		public Serializer(IFactory<T> factory) {
			this.factory = factory;
		}
		
		@Override
		public T read(ResourceLocation id, JsonObject json) {
			final String group = JSONUtils.getString(json, "group", "");
			final Ingredient ingredient;
			if (JSONUtils.isJsonArray(json, "ingredient")) {
				ingredient = Ingredient.deserialize(JSONUtils.getJsonArray(json, "ingredient"));
			} else {
				ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
			}
			final int fuel = JSONUtils.getInt(json, "fuel");
			return factory.create(id, group, ingredient, fuel);
		}
		
		@Override
		public T read(ResourceLocation id, PacketBuffer buffer) {
			final String group = buffer.readString(32767);
			final Ingredient ingredient = Ingredient.read(buffer);
			final int fuel = buffer.readInt();
			return factory.create(id, group, ingredient, fuel);
		}
		
		@Override
		public void write(PacketBuffer buffer, T recipe) {
			buffer.writeString(recipe.group);
			recipe.ingredient.write(buffer);
			buffer.writeInt(recipe.fuel);
		}
		
		public static interface IFactory<T extends FuelRecipe> {
			
			T create(ResourceLocation id, String group, Ingredient ingredient, int fuel);
		}
	}
}
