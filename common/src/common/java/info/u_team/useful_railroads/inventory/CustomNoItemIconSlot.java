package info.u_team.useful_railroads.inventory;

import com.mojang.datafixers.util.Pair;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class CustomNoItemIconSlot extends Slot {
	
	private final Pair<ResourceLocation, ResourceLocation> noItemIcon;
	
	public CustomNoItemIconSlot(Container container, ResourceLocation atlasLocation, ResourceLocation location, int index, int x, int y) {
		super(container, index, x, y);
		noItemIcon = Pair.of(atlasLocation, location);
	}
	
	@Override
	public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
		return noItemIcon;
	}
	
}
