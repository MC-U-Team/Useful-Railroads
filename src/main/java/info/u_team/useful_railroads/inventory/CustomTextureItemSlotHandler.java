package info.u_team.useful_railroads.inventory;

import info.u_team.u_team_core.container.ItemSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class CustomTextureItemSlotHandler extends ItemSlot {
	
	public CustomTextureItemSlotHandler(IItemHandler itemHandler, ResourceLocation atlasLocation, ResourceLocation location, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		setBackground(atlasLocation, location);
	}
	
}
