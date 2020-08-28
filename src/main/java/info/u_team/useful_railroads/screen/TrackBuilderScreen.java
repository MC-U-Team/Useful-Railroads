package info.u_team.useful_railroads.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.u_team_core.gui.elements.BetterButton;
import info.u_team.u_team_core.screen.UContainerScreen;
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
		
		addButton(new BetterButton(guiLeft + 169, guiTop + 16, 108, 11, 0.7F, ITextComponent.func_244388_a(null), button -> {
			container.getChangeModeMessage().triggerMessage();
		}) {
			
			@Override
			public ITextComponent getMessage() {
				return ITextComponent.func_244388_a(container.getWrapper().getMode().getDisplayString());
			}
		});
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		buttons.forEach(button -> button.renderToolTip(matrixStack, mouseX, mouseY));
		func_230459_a_(matrixStack, mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		font.func_243248_b(matrixStack, title, 8, 6, 4210752);
		font.func_243248_b(matrixStack, playerInventory.getDisplayName(), 62, ySize - 94, 4210752);
		
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
