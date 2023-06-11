package info.u_team.useful_railroads.screen;

import info.u_team.u_team_core.gui.elements.UButton;
import info.u_team.u_team_core.screen.UContainerMenuScreen;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.menu.TrackBuilderMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TrackBuilderScreen extends UContainerMenuScreen<TrackBuilderMenu> {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(UsefulRailroadsMod.MODID, "textures/gui/track_builder.png");
	
	private final Component modeTextComponent;
	private final Component railsTextComponent;
	private final Component groundBlocksTextComponent;
	private final Component tunnelBlocksTextComponent;
	private final Component redstoneTorchesTextComponent;
	private final Component torchesTextComponent;
	private final Component fuelTextComponent;
	
	public TrackBuilderScreen(TrackBuilderMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title, BACKGROUND, 284, 296);
		backgroundWidth = backgroundHeight = 512;
		setTextLocation(8, 6, 62, imageHeight - 94);
		
		final String langKey = "container.usefulrailroads.track_builder.";
		
		modeTextComponent = Component.translatable(langKey + "mode");
		railsTextComponent = Component.translatable(langKey + "rails");
		groundBlocksTextComponent = Component.translatable(langKey + "ground_blocks");
		tunnelBlocksTextComponent = Component.translatable(langKey + "tunnel_blocks");
		redstoneTorchesTextComponent = Component.translatable(langKey + "redstone_torches");
		torchesTextComponent = Component.translatable(langKey + "torches");
		fuelTextComponent = Component.translatable(langKey + "fuel");
	}
	
	@Override
	protected void init() {
		super.init();
		
		final UButton button = addRenderableWidget(new UButton(leftPos + 169, topPos + 16, 108, 11, Component.empty()) {
			
			@Override
			public Component getMessage() {
				return menu.getWrapper().getMode().getDisplayComponent();
			}
		});
		button.setPressable(() -> menu.getChangeModeMessage().triggerMessage());
		button.setScale(0.7F);
	}
	
	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		super.renderLabels(guiGraphics, mouseX, mouseY);
		
		guiGraphics.drawString(font, modeTextComponent, 169, 6, 0x404040, false);
		
		guiGraphics.drawString(font, railsTextComponent, 8, 20, 0x404040, false);
		guiGraphics.drawString(font, groundBlocksTextComponent, 8, 52, 0x404040, false);
		guiGraphics.drawString(font, tunnelBlocksTextComponent, 8, 102, 0x404040, false);
		guiGraphics.drawString(font, redstoneTorchesTextComponent, 8, 170, 0x404040, false);
		guiGraphics.drawString(font, torchesTextComponent, 116, 170, 0x404040, false);
		
		final Component fuelComponent = fuelTextComponent.plainCopy().append(": ").append(Component.literal(Integer.toString(menu.getWrapper().getFuel())).withStyle(ChatFormatting.DARK_AQUA));
		
		guiGraphics.drawString(font, fuelComponent, imageWidth - font.width(fuelComponent) - 6, 170, 0x404040, false);
	}
	
}
