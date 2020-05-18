package info.u_team.useful_railroads.handler;

import java.util.Collection;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.item.StandardGaugeBuilder;
import info.u_team.useful_railroads.item.TrackBuilderItem;
import info.u_team.useful_railroads.util.TrackBuilderManager;
import info.u_team.useful_railroads.util.TrackBuilderMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent.HighlightBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class DrawTrackBuilderSelection {
	
	@SubscribeEvent
	public static void onBlockHighlight(HighlightBlock event) {
		final PlayerEntity player = Minecraft.getInstance().player;
		final ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);
				
		if(stack.getItem() instanceof StandardGaugeBuilder) {
			drawSleeperTool(event);
			return;
		}

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
		
		TrackBuilderManager.create(rayTraceResult.getPos(), rayTraceResult.getFace(), player.world, new Vec3d(event.getInfo().getViewVector()), mode, doubleTrack).ifPresent(manager -> {
			final int red;
			final int blue;
			if (player.isSneaking()) {
				red = 1;
				blue = 0;
			} else {
				red = 0;
				blue = 1;
			}
			
			final MatrixStack matrixStack = event.getMatrix();
			
			final Vec3d projectedView = event.getInfo().getProjectedView();
			
			drawSelectionBox(matrixStack, projectedView, manager.getAllPositionsSet(), red, 0, blue, 1);
			drawSelectionBox(matrixStack, projectedView, manager.getFirstRailPos(), 0, 1, 0, 1);
			event.setCanceled(true);
		});
	}
	
	public static void drawSelectionBox(MatrixStack stack, Vec3d projectedView, Collection<BlockPos> posList, float red, float green, float blue, float alpha) {
		final IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
		
		posList.forEach(pos -> {
			
			final IVertexBuilder builder = buffer.getBuffer(RenderType.getLines());
			WorldRenderer.drawShape(stack, builder, VoxelShapes.fullCube(), pos.getX() - projectedView.x, pos.getY() - projectedView.y, pos.getZ() - projectedView.z, red, green, blue, alpha);
			buffer.finish();
		});
	}
	
	public static void drawSleeperTool(final HighlightBlock event) {
		final BlockRayTraceResult rayTraceResult = (BlockRayTraceResult) event.getTarget();
		final BlockPos pos = rayTraceResult.getPos().up();		
		@SuppressWarnings("resource")
		final PlayerEntity player = Minecraft.getInstance().player;
		final BlockPos nextpos = pos.offset(player.getHorizontalFacing().rotateY());

		final MatrixStack matrixStack = event.getMatrix();
		final Vec3d projectedView = event.getInfo().getProjectedView();

		drawSelectionBox(matrixStack, projectedView, Lists.newArrayList(pos, nextpos), 1, 0, 0, 1);
		event.setCanceled(true);
	}
	
}
