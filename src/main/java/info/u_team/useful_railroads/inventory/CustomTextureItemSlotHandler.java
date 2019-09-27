package info.u_team.useful_railroads.inventory;

import info.u_team.useful_railroads.util.AtlasSpriteMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.items.*;

public class CustomTextureItemSlotHandler extends SlotItemHandler {
	
	private final ResourceLocation id;
	
	public CustomTextureItemSlotHandler(IItemHandler itemHandler, ResourceLocation id, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.id = id;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public TextureAtlasSprite getBackgroundSprite() {
		return SpriteMap.SPRITE_MAP.getSprite(id);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public ResourceLocation getBackgroundLocation() {
		return id;
	}
	
	// The variable is in an extra class for server compatibility
	@OnlyIn(Dist.CLIENT)
	private static class SpriteMap {
		
		static final AtlasSpriteMap SPRITE_MAP = new AtlasSpriteMap();
	}
	
}
