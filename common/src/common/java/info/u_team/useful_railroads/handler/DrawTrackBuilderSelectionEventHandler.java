package info.u_team.useful_railroads.handler;

import java.util.Collection;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import info.u_team.u_team_core.api.event.ClientEvents;
import info.u_team.useful_railroads.component.TrackBuilderComponent;
import info.u_team.useful_railroads.init.UsefulRailroadsDataComponentTypes;
import info.u_team.useful_railroads.item.TrackBuilderItem;
import info.u_team.useful_railroads.util.TrackBuilderManager;
import info.u_team.useful_railroads.util.TrackBuilderMode;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
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
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

public class DrawTrackBuilderSelectionEventHandler {
	
	private static boolean onRenderBlockOutline(LevelRenderer levelRenderer, Camera camera, BlockHitResult target, DeltaTracker deltaTracker, PoseStack poseStack, MultiBufferSource bufferSource) {
		final Player player = Minecraft.getInstance().player;
		final ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
		
		if (!(stack.getItem() instanceof TrackBuilderItem item)) {
			return true;
		}
		
		final boolean doubleTrack = item.isDoubleTrack();
		
		final TrackBuilderComponent component = stack.get(UsefulRailroadsDataComponentTypes.TRACK_BUILDER.get());
		final TrackBuilderMode mode;
		if (component != null) {
			mode = component.getMode();
		} else {
			mode = TrackBuilderMode.MODE_NOAIR;
		}
		
		return TrackBuilderManager.create(target.getBlockPos(), target.getDirection(), player.level(), new Vec3(camera.getLookVector()), mode, doubleTrack).map(manager -> {
			final int red;
			final int blue;
			if (player.isShiftKeyDown()) {
				red = 1;
				blue = 0;
			} else {
				red = 0;
				blue = 1;
			}
			
			final Vec3 projectedView = camera.getPosition();
			
			final BufferSource buffer = MultiBufferSource.immediate(new ByteBufferBuilder(1536));
			final VertexConsumer builder = buffer.getBuffer(RenderType.lines());
			
			drawSelectionBox(poseStack, builder, projectedView, manager.getAllPositionsSet(), red, 0, blue, 1);
			drawSelectionBox(poseStack, builder, projectedView, manager.getFirstRailPos(), 0, 1, 0, 1);
			
			buffer.endBatch();
			
			return false;
		}).orElse(true);
	}
	
	private static void drawSelectionBox(PoseStack poseStack, VertexConsumer builder, Vec3 projectedView, Collection<BlockPos> posList, float red, float green, float blue, float alpha) {
		posList.forEach(pos -> {
			LevelRenderer.renderShape(poseStack, builder, Shapes.block(), pos.getX() - projectedView.x, pos.getY() - projectedView.y, pos.getZ() - projectedView.z, red, green, blue, alpha);
		});
	}
	
	static void register() {
		ClientEvents.registerRenderBlockOutline(DrawTrackBuilderSelectionEventHandler::onRenderBlockOutline);
	}
}
