package info.u_team.useful_railroads.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.u_team_core.gui.elements.BetterButton;
import info.u_team.u_team_core.screen.UBasicContainerScreen;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.container.TrackBuilderContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class TrackBuilderScreen extends UBasicContainerScreen<TrackBuilderContainer> {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(UsefulRailroadsMod.MODID, "textures/gui/track_builder.png");
	
	public TrackBuilderScreen(TrackBuilderContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title, BACKGROUND, 284, 296);
		backgroundWidth = backgroundHeight = 512;
		setTextLocation(8, 6, 62, ySize - 94);
	}
	
	@Override
	protected void init() {
		super.init();
		
		addButton(new BetterButton(guiLeft + 169, guiTop + 16, 108, 11, 0.7F, ITextComponent.getTextComponentOrEmpty(null), button -> {
			container.getChangeModeMessage().triggerMessage();
		}) {
			
			@Override
			public ITextComponent getMessage() {
				return ITextComponent.getTextComponentOrEmpty(container.getWrapper().getMode().getDisplayString());
			}
		});
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
		
		final String langKey = "container.usefulrailroads.track_builder.";
		
		font.drawString(matrixStack, I18n.format(langKey + "mode"), 169, 6, 4210752);
		
		font.drawString(matrixStack, I18n.format(langKey + "rails"), 8, 20, 4210752);
		font.drawString(matrixStack, I18n.format(langKey + "ground_blocks"), 8, 52, 4210752);
		font.drawString(matrixStack, I18n.format(langKey + "tunnel_blocks"), 8, 102, 4210752);
		font.drawString(matrixStack, I18n.format(langKey + "redstone_torches"), 8, 170, 4210752);
		font.drawString(matrixStack, I18n.format(langKey + "torches"), 116, 170, 4210752);
		
		final String fuelString = I18n.format(langKey + "fuel") + ": " + TextFormatting.DARK_AQUA + container.getWrapper().getFuel();
		
		font.drawString(matrixStack, fuelString, xSize - font.getStringWidth(fuelString) - 6, 170, 4210752);
	}
	
}
