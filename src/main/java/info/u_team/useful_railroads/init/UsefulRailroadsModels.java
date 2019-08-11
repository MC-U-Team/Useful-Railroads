package info.u_team.useful_railroads.init;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.HIGHSPEED_RAIL;

import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.util.ModelUtil;
import net.minecraft.block.*;
import net.minecraft.state.*;
import net.minecraft.state.properties.RailShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class UsefulRailroadsModels {
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void register(Register<Block> event) {
		ModelUtil.addCustomStateContainer(HIGHSPEED_RAIL.getRegistryName(), (new StateContainer.Builder<Block, BlockState>(HIGHSPEED_RAIL)).add(PoweredRailBlock.POWERED, EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST)).create(BlockState::new));
	}
	
}
