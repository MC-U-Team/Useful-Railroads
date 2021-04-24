package info.u_team.useful_railroads.inventory;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

import info.u_team.useful_railroads.recipe.FuelRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;

public class FuelItemHandler<T extends FuelRecipe> implements IItemHandlerModifiable {
	
	private final IRecipeType<T> recipeType;
	
	private final Supplier<World> worldSupplier;
	
	private final BooleanSupplier canAddFuel;
	private final IntConsumer fuelAdder;
	
	private T currentRecipe;
	private ItemStack failedMatch = ItemStack.EMPTY;
	
	public FuelItemHandler(IRecipeType<T> recipeType, Supplier<World> worldSupplier, IntConsumer fuelAdder) {
		this(recipeType, worldSupplier, () -> true, fuelAdder);
	}
	
	public FuelItemHandler(IRecipeType<T> recipeType, Supplier<World> worldSupplier, BooleanSupplier canAddFuel, IntConsumer fuelAdder) {
		this.recipeType = recipeType;
		this.worldSupplier = worldSupplier;
		this.canAddFuel = canAddFuel;
		this.fuelAdder = fuelAdder;
	}
	
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return canAddFuel.getAsBoolean() && getRecipe(stack, worldSupplier.get()).isPresent();
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
		final World world = worldSupplier.get();
		if (!world.isRemote) {
			getRecipe(stack, world).ifPresent(recipe -> fuelAdder.accept(stack.getCount() * recipe.getFuel()));
		}
	}
	
	private Optional<T> getRecipe(ItemStack stack, World world) {
		final IInventory inventory = new Inventory(stack);
		if (stack.isEmpty() || stack == failedMatch)
			return Optional.empty();
		if (currentRecipe != null && currentRecipe.matches(inventory, world)) {
			return Optional.of(currentRecipe);
		} else {
			final T recipe = world.getRecipeManager().getRecipe(recipeType, inventory, world).orElse(null);
			if (recipe == null) {
				failedMatch = stack;
			} else {
				failedMatch = ItemStack.EMPTY;
			}
			return Optional.ofNullable(currentRecipe = recipe);
		}
	}
}
