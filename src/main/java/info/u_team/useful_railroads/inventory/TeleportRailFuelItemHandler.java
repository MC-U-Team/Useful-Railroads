package info.u_team.useful_railroads.inventory;

import java.util.Optional;
import java.util.function.Supplier;

import info.u_team.useful_railroads.init.UsefulRailroadsRecipeTypes;
import info.u_team.useful_railroads.recipe.TeleportRailFuelRecipe;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;

public class TeleportRailFuelItemHandler implements IItemHandlerModifiable {
	
	private final Supplier<World> worldSupplier;
	private final Consumer<Integer> fuelAdder;
	
	private TeleportRailFuelRecipe currentRecipe;
	private ItemStack failedMatch = ItemStack.EMPTY;
	
	public TeleportRailFuelItemHandler(Supplier<World> worldSupplier, Consumer<Integer> fuelAdder) {
		this.worldSupplier = worldSupplier;
		this.fuelAdder = fuelAdder;
	}
	
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return getRecipe(stack, worldSupplier.get()).isPresent();
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
	
	private Optional<TeleportRailFuelRecipe> getRecipe(ItemStack stack, World world) {
		final IInventory inventory = new Inventory(stack);
		if (stack.isEmpty() || stack == failedMatch)
			return Optional.empty();
		if (currentRecipe != null && currentRecipe.matches(inventory, world)) {
			return Optional.of(currentRecipe);
		} else {
			final TeleportRailFuelRecipe recipe = world.getRecipeManager().getRecipe(UsefulRailroadsRecipeTypes.TELEPORT_FUEL, inventory, world).orElse(null);
			if (recipe == null) {
				failedMatch = stack;
			} else {
				failedMatch = ItemStack.EMPTY;
			}
			return Optional.ofNullable(currentRecipe = recipe);
		}
	}
}
