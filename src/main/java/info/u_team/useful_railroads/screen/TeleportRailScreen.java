package info.u_team.useful_railroads.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.u_team_core.screen.UBasicContainerScreen;
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
public class TeleportRailScreen extends UBasicContainerScreen<TeleportRailContainer> {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(UsefulRailroadsMod.MODID, "textures/gui/teleport_rail.png");
	
	private final ITextComponent dimensionTextComponent;
	private final ITextComponent xTextComponent;
	private final ITextComponent yTextComponent;
	private final ITextComponent zTextComponent;
	private final ITextComponent fuelTextComponent;
	private final ITextComponent consumptionTextComponent;
	private final ITextComponent seperatorTextComponent;
	
	public TeleportRailScreen(TeleportRailContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title, BACKGROUND, 176, 189);
		
		final String langKey = "container.usefulrailroads.teleport_rail.";
		
		dimensionTextComponent = new TranslationTextComponent(langKey + "dimension");
		xTextComponent = new TranslationTextComponent(langKey + "x");
		yTextComponent = new TranslationTextComponent(langKey + "y");
		zTextComponent = new TranslationTextComponent(langKey + "z");
		fuelTextComponent = new TranslationTextComponent(langKey + "fuel");
		consumptionTextComponent = new TranslationTextComponent(langKey + "consumption");
		seperatorTextComponent = new StringTextComponent(": ");
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
		
		final TeleportRailTileEntity tileEntity = getContainer().getTileEntity();
		final Location location = tileEntity.getLocation();
		final BlockPos pos = location.getPos();
		
		font.func_243248_b(matrixStack, dimensionTextComponent.copyRaw().append(seperatorTextComponent).append(new StringTextComponent(location.getRegistryKey().getLocation().toString()).mergeStyle(TextFormatting.DARK_GREEN)), 11, 23, 0x404040);
		font.func_243248_b(matrixStack, xTextComponent.copyRaw().append(seperatorTextComponent).append(new StringTextComponent(Integer.toString(pos.getX())).mergeStyle(TextFormatting.DARK_GREEN)), 11, 33, 0x404040);
		font.func_243248_b(matrixStack, yTextComponent.copyRaw().append(seperatorTextComponent).append(new StringTextComponent(Integer.toString(pos.getY())).mergeStyle(TextFormatting.DARK_GREEN)), 11, 43, 0x404040);
		font.func_243248_b(matrixStack, zTextComponent.copyRaw().append(seperatorTextComponent).append(new StringTextComponent(Integer.toString(pos.getZ())).mergeStyle(TextFormatting.DARK_GREEN)), 11, 53, 0x404040);
		
		font.func_243248_b(matrixStack, fuelTextComponent.copyRaw().append(seperatorTextComponent).append(new StringTextComponent(Integer.toString(tileEntity.getFuel())).mergeStyle(TextFormatting.DARK_AQUA)), 11, 68, 0x404040);
		font.func_243248_b(matrixStack, consumptionTextComponent.copyRaw().append(seperatorTextComponent).append(new StringTextComponent(Integer.toString(tileEntity.getCost())).mergeStyle(TextFormatting.DARK_RED)), 11, 78, 0x404040);
	}
	
}
