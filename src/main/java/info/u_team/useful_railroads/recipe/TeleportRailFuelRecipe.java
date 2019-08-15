package info.u_team.useful_railroads.recipe;

import com.google.gson.JsonObject;

import info.u_team.u_team_core.recipeserializer.URecipeSerializer;
import info.u_team.useful_railroads.init.*;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class TeleportRailFuelRecipe implements IRecipe<IInventory> {
	
	protected final Ingredient ingredient;
	protected final int fuel;
	protected final ResourceLocation id;
	protected final String group;
	
	public TeleportRailFuelRecipe(ResourceLocation id, String group, Ingredient ingredient, int fuel) {
		this.id = id;
		this.group = group;
		this.ingredient = ingredient;
		this.fuel = fuel;
	}
	
	public IRecipeType<?> getType() {
		return UsefulRailroadsRecipeTypes.TELEPORT_FUEL;
	}
	
	public IRecipeSerializer<?> getSerializer() {
		return UsefulRailroadsRecipeSerializers.TELEPORT_FUEL;
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
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(ingredient);
		return nonnulllist;
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
	
	public static class Serializer extends URecipeSerializer<TeleportRailFuelRecipe> {
		
		public Serializer(String name) {
			super(name);
		}
		
		@Override
		public TeleportRailFuelRecipe read(ResourceLocation id, JsonObject json) {
			final String group = JSONUtils.getString(json, "group", "");
			final Ingredient ingredient;
			if (JSONUtils.isJsonArray(json, "ingredient")) {
				ingredient = Ingredient.deserialize(JSONUtils.getJsonArray(json, "ingredient"));
			} else {
				ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
			}
			final int fuel = JSONUtils.getInt(json, "fuel");
			return new TeleportRailFuelRecipe(id, group, ingredient, fuel);
		}
		
		@Override
		public TeleportRailFuelRecipe read(ResourceLocation id, PacketBuffer buffer) {
			final String group = buffer.readString(32767);
			final Ingredient ingredient = Ingredient.read(buffer);
			final int fuel = buffer.readInt();
			return new TeleportRailFuelRecipe(id, group, ingredient, fuel);
		}
		
		@Override
		public void write(PacketBuffer buffer, TeleportRailFuelRecipe recipe) {
			buffer.writeString(recipe.group);
			recipe.ingredient.write(buffer);
			buffer.writeInt(recipe.fuel);
		}
	}
}
