package info.u_team.useful_railroads.screen;

import info.u_team.u_team_core.gui.UContainerScreen;
import info.u_team.u_team_core.gui.elements.BetterButton;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.container.TrackBuilderContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class TrackBuilderScreen extends UContainerScreen<TrackBuilderContainer> {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(UsefulRailroadsMod.MODID, "textures/gui/track_builder.png");
	
	public TrackBuilderScreen(TrackBuilderContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title, BACKGROUND);
		backgroundWidth = backgroundHeight = 512;
		xSize = 284;
		ySize = 296;
	}
	
	@Override
	protected void init() {
		super.init();
		
		addButton(new BetterButton(guiLeft + 169, guiTop + 16, 108, 11, 0.7F, "", button -> {
			container.getChangeModeMessage().triggerMessage();
		}) {
			
			@Override
			public String getMessage() {
				return container.getWrapper().getMode().getDisplayString();
			}
		});
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		buttons.forEach(button -> button.renderToolTip(mouseX, mouseY));
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		font.drawString(title.getFormattedText(), 8, 6, 4210752);
		font.drawString(playerInventory.getDisplayName().getFormattedText(), 62, ySize - 94, 4210752);
		
		final String langKey = "container.usefulrailroads.track_builder.";
		
		font.drawString(I18n.format(langKey + "mode"), 169, 6, 4210752);
		
		font.drawString(I18n.format(langKey + "rails"), 8, 20, 4210752);
		font.drawString(I18n.format(langKey + "ground_blocks"), 8, 52, 4210752);
		font.drawString(I18n.format(langKey + "tunnel_blocks"), 8, 102, 4210752);
		font.drawString(I18n.format(langKey + "redstone_torches"), 8, 170, 4210752);
		font.drawString(I18n.format(langKey + "torches"), 116, 170, 4210752);
		
		final String fuelString = I18n.format(langKey + "fuel") + ": " + TextFormatting.DARK_AQUA + container.getWrapper().getFuel();
		
		font.drawString(fuelString, xSize - font.getStringWidth(fuelString) - 6, 170, 4210752);
	}
	
}
