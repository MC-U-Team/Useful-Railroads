package info.u_team.useful_railroads.inventory;

import info.u_team.useful_railroads.UsefulRailroadsReference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

public class FuelItemSlot extends CustomNoItemIconSlot {
	
	private static final ResourceLocation BLOCK_ATLAS = InventoryMenu.BLOCK_ATLAS;
	private static final ResourceLocation EMPTY_FUEL_SLOT = ResourceLocation.fromNamespaceAndPath(UsefulRailroadsReference.MODID, "item/empty_fuel_slot");
	
	public FuelItemSlot(Container container, int index, int x, int y) {
		super(container, BLOCK_ATLAS, EMPTY_FUEL_SLOT, index, x, y);
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return container.canPlaceItem(index, stack);
	}
	
	@Override
	public boolean mayPickup(Player player) {
		return false;
	}
}
