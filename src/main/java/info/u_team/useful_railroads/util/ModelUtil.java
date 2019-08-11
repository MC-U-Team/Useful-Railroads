package info.u_team.useful_railroads.util;

import static net.minecraft.client.renderer.model.ModelBakery.STATE_CONTAINER_OVERRIDES;

import java.util.HashMap;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.*;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;

public class ModelUtil {
	
	static {
		if (STATE_CONTAINER_OVERRIDES instanceof ImmutableMap) {
			STATE_CONTAINER_OVERRIDES = new HashMap<ResourceLocation, StateContainer<Block, BlockState>>();
		}
	}
	
	public static void addCustomStateContainer(ResourceLocation location, StateContainer<Block, BlockState> container) {
		STATE_CONTAINER_OVERRIDES.put(location, container);
	}
	
}
