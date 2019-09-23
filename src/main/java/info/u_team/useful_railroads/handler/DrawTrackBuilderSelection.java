package info.u_team.useful_railroads.handler;

import java.util.Collection;

import com.mojang.blaze3d.platform.GlStateManager;

import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.init.UsefulRailroadsItems;
import info.u_team.useful_railroads.util.TrackBuilderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class DrawTrackBuilderSelection {
	
	@SubscribeEvent
	public static void onBblockHighlight(DrawBlockHighlightEvent event) {
		final PlayerEntity player = Minecraft.getInstance().player;
		
		if (player.getHeldItem(Hand.MAIN_HAND).getItem() != UsefulRailroadsItems.TRACK_BUILDER) {
			return;
		}
		
		if (event.getTarget().getType() != RayTraceResult.Type.BLOCK) {
			return;
		}
		final BlockRayTraceResult rayTraceResult = (BlockRayTraceResult) event.getTarget();
		
		final TrackBuilderManager manager = new TrackBuilderManager(rayTraceResult.getPos(), rayTraceResult.getFace(), player.world, event.getInfo().getLookDirection());
		
		if (!manager.hasErrorOccured()) {
			final int red;
			final int blue;
			if (player.isSneaking()) {
				red = 1;
				blue = 0;
			} else {
				red = 0;
				blue = 1;
			}
			drawSelectionBox(event.getInfo().getProjectedView(), manager.getRenderSet(), red, 0, blue, 1);
			event.setCanceled(true);
		}
	}
	
	public static void drawSelectionBox(Vec3d projectedView, Collection<BlockPos> posList, float red, float green, float blue, float alpha) {
		GlStateManager.enableBlend();
		GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.lineWidth(Math.max(2.5F, Minecraft.getInstance().mainWindow.getFramebufferWidth() / 1920.0F * 2.5F));
		GlStateManager.disableTexture();
		GlStateManager.depthMask(false);
		GlStateManager.matrixMode(5889);
		GlStateManager.pushMatrix();
		GlStateManager.scalef(1, 1, 0.999F);
		
		posList.forEach(pos -> WorldRenderer.drawShape(VoxelShapes.fullCube(), pos.getX() - projectedView.x, pos.getY() - projectedView.y, pos.getZ() - projectedView.z, red, green, blue, alpha));
		
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture();
		GlStateManager.disableBlend();
		
	}
	
}
