package info.u_team.useful_railroads.inventory;

import info.u_team.useful_railroads.util.AtlasSpriteMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.items.*;

public class CustomTextureItemSlotHandler extends SlotItemHandler {
	
	@OnlyIn(Dist.CLIENT)
	private static final AtlasSpriteMap SPRITE_MAP = new AtlasSpriteMap();
	
	private final ResourceLocation id;
	
	public CustomTextureItemSlotHandler(IItemHandler itemHandler, ResourceLocation id, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.id = id;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public TextureAtlasSprite getBackgroundSprite() {
		return SPRITE_MAP.getSprite(id);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public ResourceLocation getBackgroundLocation() {
		return id;
	}
	
}
