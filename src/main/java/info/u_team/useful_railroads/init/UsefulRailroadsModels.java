package info.u_team.useful_railroads.init;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.*;

import info.u_team.u_team_core.util.ModelUtil;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.block.DirectionRailBlock;
import net.minecraft.block.*;
import net.minecraft.state.*;
import net.minecraft.state.properties.RailShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class UsefulRailroadsModels {
	
	@SubscribeEvent
	public static void register(FMLClientSetupEvent event) {
		ModelUtil.addCustomStateContainer(HIGHSPEED_RAIL.getRegistryName(), (new StateContainer.Builder<Block, BlockState>(HIGHSPEED_RAIL)).add(PoweredRailBlock.POWERED, EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST)).create(BlockState::new));
		ModelUtil.addCustomStateContainer(DIRECTION_RAIL.getRegistryName(), (new StateContainer.Builder<Block, BlockState>(DIRECTION_RAIL)).add(PoweredRailBlock.POWERED, EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST)).add(DirectionRailBlock.AXIS_DIRECTION).create(BlockState::new));
		ModelUtil.addCustomStateContainer(TELEPORT_RAIL.getRegistryName(), (new StateContainer.Builder<Block, BlockState>(TELEPORT_RAIL)).add(PoweredRailBlock.POWERED, EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST)).create(BlockState::new));
	}
	
}
