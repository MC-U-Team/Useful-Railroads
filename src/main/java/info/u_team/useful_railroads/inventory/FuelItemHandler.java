package info.u_team.useful_railroads.inventory;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

import info.u_team.useful_railroads.recipe.FuelRecipe;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandlerModifiable;

public class FuelItemHandler<T extends FuelRecipe> implements IItemHandlerModifiable {
	
	private final RecipeType<T> recipeType;
	
	private final Supplier<Level> levelSupplier;
	
	private final BooleanSupplier canAddFuel;
	private final IntConsumer fuelAdder;
	
	private T currentRecipe;
	private ItemStack failedMatch = ItemStack.EMPTY;
	
	public FuelItemHandler(RecipeType<T> recipeType, Supplier<Level> levelSupplier, IntConsumer fuelAdder) {
		this(recipeType, levelSupplier, () -> true, fuelAdder);
	}
	
	public FuelItemHandler(RecipeType<T> recipeType, Supplier<Level> levelSupplier, BooleanSupplier canAddFuel, IntConsumer fuelAdder) {
		this.recipeType = recipeType;
		this.levelSupplier = levelSupplier;
		this.canAddFuel = canAddFuel;
		this.fuelAdder = fuelAdder;
	}
	
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return canAddFuel.getAsBoolean() && getRecipe(stack, levelSupplier.get()).isPresent();
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		
		if (!isItemValid(slot, stack)) {
			return stack;
		}
		if (!simulate) {
			setStackInSlot(slot, stack);
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public int getSlots() {
		return 1;
	}
	
	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		final Level world = levelSupplier.get();
		if (!world.isClientSide) {
			getRecipe(stack, world).ifPresent(recipe -> fuelAdder.accept(stack.getCount() * recipe.getFuel()));
		}
	}
	
	private Optional<T> getRecipe(ItemStack stack, Level world) {
		final SimpleContainer inventory = new SimpleContainer(stack);
		if (stack.isEmpty() || stack == failedMatch)
			return Optional.empty();
		if (currentRecipe != null && currentRecipe.matches(inventory, world)) {
			return Optional.of(currentRecipe);
		} else {
			final T recipe = world.getRecipeManager().getRecipeFor(recipeType, inventory, world).map(RecipeHolder::value).orElse(null);
			if (recipe == null) {
				failedMatch = stack;
			} else {
				failedMatch = ItemStack.EMPTY;
			}
			return Optional.ofNullable(currentRecipe = recipe);
		}
	}
}
