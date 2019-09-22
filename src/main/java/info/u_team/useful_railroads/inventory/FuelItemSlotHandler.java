package info.u_team.useful_railroads.inventory;

import info.u_team.useful_railroads.UsefulRailroadsMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class FuelItemSlotHandler extends CustomTextureItemSlotHandler {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(UsefulRailroadsMod.MODID, "textures/item/empty_fuel_slot.png");
	
	public FuelItemSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, TEXTURE, index, xPosition, yPosition);
	}
	
}
