package info.u_team.useful_railroads.handler;

import java.util.Collection;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;

import info.u_team.useful_railroads.item.TrackBuilderItem;
import info.u_team.useful_railroads.util.TrackBuilderManager;
import info.u_team.useful_railroads.util.TrackBuilderMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.client.event.RenderHighlightEvent.Block;
import net.minecraftforge.eventbus.api.IEventBus;

public class DrawTrackBuilderSelectionEventHandler {
	
	private static void onBlockHighlight(Block event) {
		final Player player = Minecraft.getInstance().player;
		final ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
		
		if (!(stack.getItem() instanceof TrackBuilderItem)) {
			return;
		}
		
		final boolean doubleTrack = ((TrackBuilderItem) stack.getItem()).isDoubleTrack();
		
		if (event.getTarget().getType() != HitResult.Type.BLOCK) {
			return;
		}
		
		final TrackBuilderMode mode;
		if (stack.hasTag()) {
			mode = TrackBuilderMode.byName(stack.getTag().getString("mode"));
		} else {
			mode = TrackBuilderMode.MODE_NOAIR;
		}
		
		final BlockHitResult rayTraceResult = event.getTarget();
		
		TrackBuilderManager.create(rayTraceResult.getBlockPos(), rayTraceResult.getDirection(), player.level(), new Vec3(event.getCamera().getLookVector()), mode, doubleTrack).ifPresent(manager -> {
			final int red;
			final int blue;
			if (player.isShiftKeyDown()) {
				red = 1;
				blue = 0;
			} else {
				red = 0;
				blue = 1;
			}
			
			final PoseStack poseStack = event.getPoseStack();
			
			final Vec3 projectedView = event.getCamera().getPosition();
			
			final BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
			final VertexConsumer builder = buffer.getBuffer(RenderType.lines());
			
			drawSelectionBox(poseStack, builder, projectedView, manager.getAllPositionsSet(), red, 0, blue, 1);
			drawSelectionBox(poseStack, builder, projectedView, manager.getFirstRailPos(), 0, 1, 0, 1);
			
			buffer.endBatch();
			
			event.setCanceled(true);
		});
	}
	
	public static void drawSelectionBox(PoseStack poseStack, VertexConsumer builder, Vec3 projectedView, Collection<BlockPos> posList, float red, float green, float blue, float alpha) {
		posList.forEach(pos -> {
			LevelRenderer.renderShape(poseStack, builder, Shapes.block(), pos.getX() - projectedView.x, pos.getY() - projectedView.y, pos.getZ() - projectedView.z, red, green, blue, alpha);
		});
	}
	
	public static void registerForge(IEventBus bus) {
		bus.addListener(DrawTrackBuilderSelectionEventHandler::onBlockHighlight);
	}
}
