package info.u_team.useful_railroads.gui;

import info.u_team.u_team_core.gui.UContainerScreen;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.container.TeleportRailContainer;
import info.u_team.useful_railroads.tileentity.TeleportRailTileEntity;
import info.u_team.useful_railroads.util.Location;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class TeleportRailScreen extends UContainerScreen<TeleportRailContainer> {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(UsefulRailroadsMod.MODID, "textures/gui/teleport_rail.png");
	
	public TeleportRailScreen(TeleportRailContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title, BACKGROUND);
		xSize = 176;
		ySize = 189;
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
		font.drawString(playerInventory.getDisplayName().getFormattedText(), 8.0F, ySize - 94, 4210752);
		
		final TeleportRailTileEntity tileEntity = getContainer().getTileEntity();
		final Location location = tileEntity.getLocation();
		final BlockPos pos = location.getPos();
		
		font.drawString("Dimension: " + TextFormatting.DARK_GREEN + location.getDimensionType().getRegistryName(), 11, 23, 4210752);
		font.drawString("X: " + TextFormatting.DARK_GREEN + pos.getX(), 11, 33, 4210752);
		font.drawString("Y: " + TextFormatting.DARK_GREEN + pos.getY(), 11, 43, 4210752);
		font.drawString("Z: " + TextFormatting.DARK_GREEN + pos.getZ(), 11, 53, 4210752);
		
		font.drawString("Fuel: " + TextFormatting.DARK_AQUA + tileEntity.getFuel(), 11, 68, 4210752);
		font.drawString("Consumption: " + TextFormatting.DARK_RED + tileEntity.getCost(), 11, 78, 4210752);
	}
	
}
