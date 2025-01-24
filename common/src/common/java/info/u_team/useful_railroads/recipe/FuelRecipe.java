package info.u_team.useful_railroads.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public abstract class FuelRecipe implements Recipe<SingleRecipeInput> {
	
	protected final Ingredient ingredient;
	protected final int fuel;
	protected final String group;
	
	public FuelRecipe(String group, Ingredient ingredient, int fuel) {
		this.group = group;
		this.ingredient = ingredient;
		this.fuel = fuel;
	}
	
	@Override
	public String getGroup() {
		return group;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
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
	public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean matches(SingleRecipeInput input, Level level) {
		return ingredient.test(input.getItem(0));
	}
	
	public int getFuel() {
		return fuel;
	}
	
	public static class Serializer<T extends FuelRecipe> implements RecipeSerializer<T> {
		
		private final MapCodec<T> codec;
		private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
		
		public Serializer(Factory<T> factory) {
			codec = RecordCodecBuilder.mapCodec(instance -> {
				return instance.group(Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> {
					return recipe.group;
				}), Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> {
					return recipe.ingredient;
				}), Codec.INT.fieldOf("fuel").orElse(0).forGetter(recipe -> {
					return recipe.fuel;
				})).apply(instance, factory::create);
			});
			streamCodec = StreamCodec.composite( //
					ByteBufCodecs.STRING_UTF8, recipe -> recipe.group, //
					Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.ingredient, //
					ByteBufCodecs.VAR_INT, recipe -> recipe.fuel, //
					factory::create);
		}
		
		@Override
		public MapCodec<T> codec() {
			return codec;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
			return streamCodec;
		}
		
		public static interface Factory<T extends FuelRecipe> {
			
			T create(String group, Ingredient ingredient, int fuel);
		}
	}
}
