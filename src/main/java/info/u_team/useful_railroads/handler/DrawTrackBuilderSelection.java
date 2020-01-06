package info.u_team.useful_railroads.handler;

import java.util.Collection;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.item.TrackBuilderItem;
import info.u_team.useful_railroads.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent.HighlightBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class DrawTrackBuilderSelection {
	
	@SubscribeEvent
	public static void onBblockHighlight(HighlightBlock event) {
		final PlayerEntity player = Minecraft.getInstance().player;
		final ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);
		
		if (!(stack.getItem() instanceof TrackBuilderItem)) {
			return;
		}
		
		final boolean doubleTrack = ((TrackBuilderItem) stack.getItem()).isDoubleTrack();
		
		if (event.getTarget().getType() != RayTraceResult.Type.BLOCK) {
			return;
		}
		
		final TrackBuilderMode mode;
		if (stack.hasTag()) {
			mode = TrackBuilderMode.byName(stack.getTag().getString("mode"));
		} else {
			mode = TrackBuilderMode.MODE_NOAIR;
		}
		
		final BlockRayTraceResult rayTraceResult = (BlockRayTraceResult) event.getTarget();
		
		TrackBuilderManager.create(rayTraceResult.getPos(), rayTraceResult.getFace(), player.world, new Vec3d(event.getInfo().func_227996_l_()), mode, doubleTrack).ifPresent(manager -> {
			final int red;
			final int blue;
			if (player.func_226563_dT_()) {
				red = 1;
				blue = 0;
			} else {
				red = 0;
				blue = 1;
			}
			drawSelectionBox(event.getInfo().getProjectedView(), manager.getAllPositionsSet(), red, 0, blue, 1);
			drawSelectionBox(event.getInfo().getProjectedView(), manager.getFirstRailPos(), 0, 1, 0, 1);
			event.setCanceled(true);
		});
	}
	
	public static void drawSelectionBox(Vec3d projectedView, Collection<BlockPos> posList, float red, float green, float blue, float alpha) {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.lineWidth(Math.max(2.5F, Minecraft.getInstance().func_228018_at_().getFramebufferWidth() / 1920.0F * 2.5F));
		RenderSystem.disableTexture();
		RenderSystem.depthMask(false);
		RenderSystem.matrixMode(5889);
		RenderSystem.pushMatrix();
		RenderSystem.scalef(1, 1, 0.999F);
		
		final IRenderTypeBuffer buffer = IRenderTypeBuffer.func_228455_a_(Tessellator.getInstance().getBuffer());
		final IVertexBuilder builder = buffer.getBuffer(RenderType.func_228659_m_());
		
		posList.forEach(pos -> WorldRenderer.func_228431_a_(new MatrixStack(), builder, VoxelShapes.fullCube(), pos.getX() - projectedView.x, pos.getY() - projectedView.y, pos.getZ() - projectedView.z, red, green, blue, alpha));
		
		RenderSystem.popMatrix();
		RenderSystem.matrixMode(5888);
		RenderSystem.depthMask(true);
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
		
	}
	
}
