package info.u_team.useful_railroads.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.u_team_core.screen.UBasicContainerScreen;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.container.TeleportRailContainer;
import info.u_team.useful_railroads.tileentity.TeleportRailTileEntity;
import info.u_team.useful_railroads.util.Location;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class TeleportRailScreen extends UBasicContainerScreen<TeleportRailContainer> {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(UsefulRailroadsMod.MODID, "textures/gui/teleport_rail.png");
	
	public TeleportRailScreen(TeleportRailContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title, BACKGROUND, 176, 189);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
		
		final TeleportRailTileEntity tileEntity = getContainer().getTileEntity();
		final Location location = tileEntity.getLocation();
		final BlockPos pos = location.getPos();
		
		final String langKey = "container.usefulrailroads.teleport_rail.";
		
		font.drawString(matrixStack, I18n.format(langKey + "dimension") + ": " + TextFormatting.DARK_GREEN + location.getRegistryKey().getLocation(), 11, 23, 4210752);
		font.drawString(matrixStack, I18n.format(langKey + "x") + ": " + TextFormatting.DARK_GREEN + pos.getX(), 11, 33, 4210752);
		font.drawString(matrixStack, I18n.format(langKey + "y") + ": " + TextFormatting.DARK_GREEN + pos.getY(), 11, 43, 4210752);
		font.drawString(matrixStack, I18n.format(langKey + "z") + ": " + TextFormatting.DARK_GREEN + pos.getZ(), 11, 53, 4210752);
		
		font.drawString(matrixStack, I18n.format(langKey + "fuel") + ": " + TextFormatting.DARK_AQUA + tileEntity.getFuel(), 11, 68, 4210752);
		font.drawString(matrixStack, I18n.format(langKey + "consumption") + ": " + TextFormatting.DARK_RED + tileEntity.getCost(), 11, 78, 4210752);
	}
	
}
