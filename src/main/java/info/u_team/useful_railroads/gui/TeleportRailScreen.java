package info.u_team.useful_railroads.gui;

import info.u_team.u_team_core.gui.UContainerScreen;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.container.TeleportRailContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class TeleportRailScreen extends UContainerScreen<TeleportRailContainer> {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(UsefulRailroadsMod.MODID, "textures/gui/rail_teleport.png");
	
	public TeleportRailScreen(TeleportRailContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title, BACKGROUND);
	}
	
}
