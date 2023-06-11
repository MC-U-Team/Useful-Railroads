package info.u_team.useful_railroads.screen;

import info.u_team.u_team_core.screen.UContainerMenuScreen;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.blockentity.TeleportRailBlockEntity;
import info.u_team.useful_railroads.menu.TeleportRailMenu;
import info.u_team.useful_railroads.util.Location;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TeleportRailScreen extends UContainerMenuScreen<TeleportRailMenu> {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(UsefulRailroadsMod.MODID, "textures/gui/teleport_rail.png");
	
	private final Component dimensionTextComponent;
	private final Component xTextComponent;
	private final Component yTextComponent;
	private final Component zTextComponent;
	private final Component fuelTextComponent;
	private final Component consumptionTextComponent;
	private final Component seperatorTextComponent;
	
	public TeleportRailScreen(TeleportRailMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title, BACKGROUND, 176, 189);
		
		final String langKey = "container.usefulrailroads.teleport_rail.";
		
		dimensionTextComponent = Component.translatable(langKey + "dimension");
		xTextComponent = Component.translatable(langKey + "x");
		yTextComponent = Component.translatable(langKey + "y");
		zTextComponent = Component.translatable(langKey + "z");
		fuelTextComponent = Component.translatable(langKey + "fuel");
		consumptionTextComponent = Component.translatable(langKey + "consumption");
		seperatorTextComponent = Component.literal(": ");
	}
	
	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		super.renderLabels(guiGraphics, mouseX, mouseY);
		
		final TeleportRailBlockEntity tileEntity = getMenu().getBlockEntity();
		final Location location = tileEntity.getLocation();
		final BlockPos pos = location.getPos();
		
		guiGraphics.drawString(font, dimensionTextComponent.plainCopy().append(seperatorTextComponent).append(Component.literal(location.getResourceKey().location().toString()).withStyle(ChatFormatting.DARK_GREEN)), 11, 23, 0x404040, false);
		guiGraphics.drawString(font, xTextComponent.plainCopy().append(seperatorTextComponent).append(Component.literal(Integer.toString(pos.getX())).withStyle(ChatFormatting.DARK_GREEN)), 11, 33, 0x404040, false);
		guiGraphics.drawString(font, yTextComponent.plainCopy().append(seperatorTextComponent).append(Component.literal(Integer.toString(pos.getY())).withStyle(ChatFormatting.DARK_GREEN)), 11, 43, 0x404040, false);
		guiGraphics.drawString(font, zTextComponent.plainCopy().append(seperatorTextComponent).append(Component.literal(Integer.toString(pos.getZ())).withStyle(ChatFormatting.DARK_GREEN)), 11, 53, 0x404040, false);
		
		guiGraphics.drawString(font, fuelTextComponent.plainCopy().append(seperatorTextComponent).append(Component.literal(Integer.toString(tileEntity.getFuel())).withStyle(ChatFormatting.DARK_AQUA)), 11, 68, 0x404040, false);
		guiGraphics.drawString(font, consumptionTextComponent.plainCopy().append(seperatorTextComponent).append(Component.literal(Integer.toString(tileEntity.getCost())).withStyle(ChatFormatting.DARK_RED)), 11, 78, 0x404040, false);
	}
	
}
