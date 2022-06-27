package info.u_team.useful_railroads.inventory;

import info.u_team.useful_railroads.UsefulRailroadsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class FuelItemSlotHandler extends CustomTextureItemSlotHandler {
	
	public FuelItemSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, new ResourceLocation("textures/atlas/blocks.png"), new ResourceLocation(UsefulRailroadsMod.MODID, "item/empty_fuel_slot"), index, xPosition, yPosition);
	}
	
}
