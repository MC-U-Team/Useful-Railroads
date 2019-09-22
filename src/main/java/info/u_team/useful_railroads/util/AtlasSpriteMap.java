package info.u_team.useful_railroads.util;

import java.util.*;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(value = Dist.CLIENT)
public class AtlasSpriteMap {
	
	private final Map<ResourceLocation, TextureAtlasSprite> map = new HashMap<>();
	
	public TextureAtlasSprite getSprite(ResourceLocation id) {
		return map.computeIfAbsent(id, location -> new BasicTextureAtlasSprite(location, 16, 16));
	}
	
	private class BasicTextureAtlasSprite extends TextureAtlasSprite {
		
		private BasicTextureAtlasSprite(ResourceLocation locationIn, int width, int height) {
			super(locationIn, width, height);
			func_217789_a(16, 16, 0, 0);
		}
		
	}
}
