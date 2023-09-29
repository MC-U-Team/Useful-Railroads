package info.u_team.useful_railroads.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public abstract class FuelRecipe implements Recipe<Container> {
	
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
	public ItemStack getResultItem(RegistryAccess registryAccess) {
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
	public ItemStack assemble(Container inv, RegistryAccess registryAccess) {
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
		
		private final Factory<T> factory;
		private final Codec<T> codec;
		
		public Serializer(Factory<T> factory) {
			this.factory = factory;
			codec = RecordCodecBuilder.create(instance -> {
				return instance.group(ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(recipe -> {
					return recipe.group;
				}), Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> {
					return recipe.ingredient;
				}), Codec.INT.fieldOf("fuel").orElse(0).forGetter(recipe -> {
					return recipe.fuel;
				})).apply(instance, factory::create);
			});
		}
		
		@Override
		public Codec<T> codec() {
			return codec;
		}
		
		@Override
		public T fromNetwork(FriendlyByteBuf buffer) {
			final String group = buffer.readUtf();
			final Ingredient ingredient = Ingredient.fromNetwork(buffer);
			final int fuel = buffer.readInt();
			return factory.create(group, ingredient, fuel);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, T recipe) {
			buffer.writeUtf(recipe.group);
			recipe.ingredient.toNetwork(buffer);
			buffer.writeInt(recipe.fuel);
		}
		
		public static interface Factory<T extends FuelRecipe> {
			
			T create(String group, Ingredient ingredient, int fuel);
		}
	}
}
