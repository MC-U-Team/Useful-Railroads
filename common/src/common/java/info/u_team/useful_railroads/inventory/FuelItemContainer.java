package info.u_team.useful_railroads.inventory;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

import info.u_team.useful_railroads.recipe.FuelRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class FuelItemContainer<T extends FuelRecipe> implements Container {
	
	private final RecipeType<T> recipeType;
	
	private final Supplier<Level> levelSupplier;
	
	private final BooleanSupplier canAddFuel;
	private final IntConsumer fuelAdder;
	
	private T currentRecipe;
	private ItemStack failedMatch = ItemStack.EMPTY;
	
	public FuelItemContainer(RecipeType<T> recipeType, Supplier<Level> levelSupplier, IntConsumer fuelAdder) {
		this(recipeType, levelSupplier, () -> true, fuelAdder);
	}
	
	public FuelItemContainer(RecipeType<T> recipeType, Supplier<Level> levelSupplier, BooleanSupplier canAddFuel, IntConsumer fuelAdder) {
		this.recipeType = recipeType;
		this.levelSupplier = levelSupplier;
		this.canAddFuel = canAddFuel;
		this.fuelAdder = fuelAdder;
	}
	
	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return canAddFuel.getAsBoolean() && getRecipe(stack, levelSupplier.get()).isPresent();
	}
	
	@Override
	public void setItem(int slot, ItemStack stack) {
		final Level world = levelSupplier.get();
		if (!world.isClientSide) {
			getRecipe(stack, world).ifPresent(recipe -> fuelAdder.accept(stack.getCount() * recipe.getFuel()));
		}
	}
	
	@Override
	public int getMaxStackSize(ItemStack stack) {
		return getMaxStackSize();
	}
	
	private Optional<T> getRecipe(ItemStack stack, Level world) {
		final SingleRecipeInput input = new SingleRecipeInput(stack);
		if (stack.isEmpty() || stack == failedMatch)
			return Optional.empty();
		if (currentRecipe != null && currentRecipe.matches(input, world)) {
			return Optional.of(currentRecipe);
		} else {
			final T recipe = world.getRecipeManager().getRecipeFor(recipeType, input, world).map(RecipeHolder::value).orElse(null);
			if (recipe == null) {
				failedMatch = stack;
			} else {
				failedMatch = ItemStack.EMPTY;
			}
			return Optional.ofNullable(currentRecipe = recipe);
		}
	}
	
	@Override
	public int getContainerSize() {
		return 1;
	}
	
	@Override
	public boolean isEmpty() {
		return true;
	}
	
	@Override
	public ItemStack getItem(int slot) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack removeItem(int slot, int amount) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canTakeItem(Container target, int slot, ItemStack stack) {
		return false;
	}
	
	@Override
	public boolean stillValid(Player player) {
		return true;
	}
	
	@Override
	public void clearContent() {
	}
	
	@Override
	public void setChanged() {
	}
}
