package info.u_team.useful_railroads.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.u_team_core.gui.elements.ScalableButton;
import info.u_team.u_team_core.screen.UBasicContainerScreen;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.container.TrackBuilderContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;

public class TrackBuilderScreen extends UBasicContainerScreen<TrackBuilderContainer> {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(UsefulRailroadsMod.MODID, "textures/gui/track_builder.png");
	
	private final ITextComponent modeTextComponent;
	private final ITextComponent railsTextComponent;
	private final ITextComponent groundBlocksTextComponent;
	private final ITextComponent tunnelBlocksTextComponent;
	private final ITextComponent redstoneTorchesTextComponent;
	private final ITextComponent torchesTextComponent;
	private final ITextComponent fuelTextComponent;
	
	public TrackBuilderScreen(TrackBuilderContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title, BACKGROUND, 284, 296);
		backgroundWidth = backgroundHeight = 512;
		setTextLocation(8, 6, 62, ySize - 94);
		
		final String langKey = "container.usefulrailroads.track_builder.";
		
		modeTextComponent = new TranslationTextComponent(langKey + "mode");
		railsTextComponent = new TranslationTextComponent(langKey + "rails");
		groundBlocksTextComponent = new TranslationTextComponent(langKey + "ground_blocks");
		tunnelBlocksTextComponent = new TranslationTextComponent(langKey + "tunnel_blocks");
		redstoneTorchesTextComponent = new TranslationTextComponent(langKey + "redstone_torches");
		torchesTextComponent = new TranslationTextComponent(langKey + "torches");
		fuelTextComponent = new TranslationTextComponent(langKey + "fuel");
	}
	
	@Override
	protected void init() {
		super.init();
		
		addButton(new ScalableButton(guiLeft + 169, guiTop + 16, 108, 11, ITextComponent.getTextComponentOrEmpty(null), 0.7F, button -> {
			container.getChangeModeMessage().triggerMessage();
		}) {
			
			@Override
			public ITextComponent getMessage() {
				return container.getWrapper().getMode().getDisplayComponent();
			}
		});
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
		
		font.func_243248_b(matrixStack, modeTextComponent, 169, 6, 0x404040);
		
		font.func_243248_b(matrixStack, railsTextComponent, 8, 20, 0x404040);
		font.func_243248_b(matrixStack, groundBlocksTextComponent, 8, 52, 0x404040);
		font.func_243248_b(matrixStack, tunnelBlocksTextComponent, 8, 102, 0x404040);
		font.func_243248_b(matrixStack, redstoneTorchesTextComponent, 8, 170, 0x404040);
		font.func_243248_b(matrixStack, torchesTextComponent, 116, 170, 0x404040);
		
		final ITextComponent fuelComponent = fuelTextComponent.copyRaw().appendString(": ").append(new StringTextComponent(Integer.toString(container.getWrapper().getFuel())).mergeStyle(TextFormatting.DARK_AQUA));
		
		font.func_243248_b(matrixStack, fuelComponent, xSize - font.getStringPropertyWidth(fuelComponent) - 6, 170, 0x404040);
	}
	
}
