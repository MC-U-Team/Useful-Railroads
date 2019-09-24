package info.u_team.useful_railroads.gui;

import info.u_team.u_team_core.gui.UContainerScreen;
import info.u_team.u_team_core.gui.elements.UButton;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.container.TrackBuilderContainer;
import info.u_team.useful_railroads.init.UsefulRailroadsNetwork;
import info.u_team.useful_railroads.network.CycleTrackBuilderMessage;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class TrackBuilderScreen extends UContainerScreen<TrackBuilderContainer> {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(UsefulRailroadsMod.MODID, "textures/gui/track_builder.png");
	
	public TrackBuilderScreen(TrackBuilderContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title, BACKGROUND);
		xSize = 176;
		ySize = 246;
	}
	
	@Override
	protected void init() {
		super.init();
		
		addButton(new UButton(guiLeft + 115, guiTop + 9, 54, 15, "", button -> {
			UsefulRailroadsNetwork.NETWORK.sendToServer(new CycleTrackBuilderMessage());
		}) {
			
			@Override
			public String getMessage() {
				return container.getWrapper().getMode().getName();
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
		font.drawString(playerInventory.getDisplayName().getFormattedText(), 8, ySize - 94, 4210752);
		
		font.drawString("Rails", 8, 20, 4210752);
		font.drawString("Ground Blocks", 8, 52, 4210752);
		font.drawString("Redstone Power", 8, 120, 4210752);
		font.drawString("Fuel: " + TextFormatting.DARK_AQUA + container.getWrapper().getFuel(), 70, 136, 4210752);
	}
	
}
