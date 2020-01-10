package info.u_team.useful_railroads.inventory;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.*;

public class CustomTextureItemSlotHandler extends SlotItemHandler {
	
	public CustomTextureItemSlotHandler(IItemHandler itemHandler, ResourceLocation atlasLocation, ResourceLocation location, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		setBackground(atlasLocation, location);
	}
	
}
