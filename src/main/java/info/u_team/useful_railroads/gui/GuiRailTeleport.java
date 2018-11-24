package info.u_team.useful_railroads.gui;

import info.u_team.useful_railroads.UsefulRailroadsConstants;
import info.u_team.useful_railroads.container.ContainerRailTeleport;
import info.u_team.useful_railroads.tilentity.TileEntityRailTeleport;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class GuiRailTeleport extends GuiContainer {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(UsefulRailroadsConstants.MODID, "textures/gui/rail_teleport.png");
	
	private TileEntityRailTeleport tile;
	
	public GuiRailTeleport(TileEntityRailTeleport tile, EntityPlayer player) {
		super(new ContainerRailTeleport(tile, player));
		this.tile = tile;
		ySize = 146;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString("Dimension: " + TextFormatting.DARK_GREEN + tile.getClientDim(), 8, 8, 4210752);
		fontRenderer.drawString("X: " + TextFormatting.DARK_GREEN + tile.getClientX(), 8, 18, 4210752);
		fontRenderer.drawString("Y: " + TextFormatting.DARK_GREEN + tile.getClientY(), 8, 28, 4210752);
		fontRenderer.drawString("Z: " + TextFormatting.DARK_GREEN + tile.getClientZ(), 8, 38, 4210752);
		
		String fuel = "Fuel: " + TextFormatting.DARK_AQUA + tile.getClientFuel();
		fontRenderer.drawString(fuel, xSize - 8 - fontRenderer.getStringWidth(fuel), 18, 4210752);
		
		String needFuel = "Consumption: " + TextFormatting.DARK_RED + tile.getClientNeedFuel();
		fontRenderer.drawString(needFuel, xSize - 8 - fontRenderer.getStringWidth(needFuel), 8, 4210752);
		
		fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(TEXTURE);
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
	}
	
}
