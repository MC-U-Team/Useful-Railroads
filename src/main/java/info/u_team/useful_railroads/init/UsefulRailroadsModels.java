package info.u_team.useful_railroads.init;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.*;

import info.u_team.u_team_core.util.ModelUtil;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.block.DirectionRailBlock;
import net.minecraft.block.*;
import net.minecraft.client.renderer.model.*;
import net.minecraft.state.*;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class UsefulRailroadsModels {
	
	private static void setup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ModelUtil.addCustomStateContainer(HIGHSPEED_RAIL.get().getRegistryName(), (new StateContainer.Builder<Block, BlockState>(HIGHSPEED_RAIL.get())).add(PoweredRailBlock.POWERED, EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST)).func_235882_a_(Block::getDefaultState, BlockState::new));
			ModelUtil.addCustomStateContainer(SPEED_CLAMP_RAIL.get().getRegistryName(), (new StateContainer.Builder<Block, BlockState>(SPEED_CLAMP_RAIL.get())).add(PoweredRailBlock.POWERED, EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST)).func_235882_a_(Block::getDefaultState, BlockState::new));
			ModelUtil.addCustomStateContainer(DIRECTION_RAIL.get().getRegistryName(), (new StateContainer.Builder<Block, BlockState>(DIRECTION_RAIL.get())).add(PoweredRailBlock.POWERED, EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST)).add(DirectionRailBlock.AXIS_DIRECTION).func_235882_a_(Block::getDefaultState, BlockState::new));
			ModelUtil.addCustomStateContainer(TELEPORT_RAIL.get().getRegistryName(), (new StateContainer.Builder<Block, BlockState>(TELEPORT_RAIL.get())).add(PoweredRailBlock.POWERED, EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST)).func_235882_a_(Block::getDefaultState, BlockState::new));
			
			ModelBakery.LOCATIONS_BUILTIN_TEXTURES.add(new RenderMaterial(new ResourceLocation("textures/atlas/blocks.png"), new ResourceLocation(UsefulRailroadsMod.MODID, "item/empty_fuel_slot")));
		});
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(UsefulRailroadsModels::setup);
	}
	
}
