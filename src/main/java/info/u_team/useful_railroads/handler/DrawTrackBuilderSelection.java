package info.u_team.useful_railroads.handler;

import com.mojang.blaze3d.platform.GlStateManager;

import info.u_team.useful_railroads.UsefulRailroadsMod;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class DrawTrackBuilderSelection {
	
	@SubscribeEvent
	public static void onBblockHighlight(DrawBlockHighlightEvent event) {
//		 drawSelectionBox(event.getInfo(), event.getTarget(), 0);
//		 event.setCanceled(true);
	}
	
	public static void drawSelectionBox(ActiveRenderInfo p_215325_1_, RayTraceResult p_215325_2_, int p_215325_3_) {
		
		if (p_215325_3_ == 0 && p_215325_2_.getType() == RayTraceResult.Type.BLOCK) {
			World world = Minecraft.getInstance().world;
			BlockPos blockpos = ((BlockRayTraceResult) p_215325_2_).getPos();
			BlockState blockstate = world.getBlockState(blockpos);
			if (!blockstate.isAir(world, blockpos) && world.getWorldBorder().contains(blockpos)) {
				GlStateManager.enableBlend();
				GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.lineWidth(Math.max(2.5F, (float) Minecraft.getInstance().mainWindow.getFramebufferWidth() / 1920.0F * 2.5F));
				GlStateManager.disableTexture();
				GlStateManager.depthMask(false);
				GlStateManager.matrixMode(5889);
				GlStateManager.pushMatrix();
				GlStateManager.scalef(1.0F, 1.0F, 0.999F);
				double d0 = p_215325_1_.getProjectedView().x;
				double d1 = p_215325_1_.getProjectedView().y;
				double d2 = p_215325_1_.getProjectedView().z;
				WorldRenderer.drawShape(VoxelShapes.create(new AxisAlignedBB(0, 0, 0, 1, 1, 1)), (double) blockpos.getX() - d0, (double) blockpos.getY() - d1, (double) blockpos.getZ() - d2, 1.0F, 0.0F, 0.0F, 1F);
				
				WorldRenderer.drawShape(VoxelShapes.create(new AxisAlignedBB(0, 0, 0, 1, 1, 1)), (double) blockpos.getX() - d0, (double) blockpos.getY() - d1 + 1, (double) blockpos.getZ() - d2, 1.0F, 0.0F, 0.0F, 1F);
				
				GlStateManager.popMatrix();
				GlStateManager.matrixMode(5888);
				GlStateManager.depthMask(true);
				GlStateManager.enableTexture();
				GlStateManager.disableBlend();
			}
		}
		
	}
	
}
