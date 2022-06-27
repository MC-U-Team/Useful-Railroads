package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.util.registry.BlockEntityTypeDeferredRegister;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.tileentity.BufferStopTileEntity;
import info.u_team.useful_railroads.tileentity.TeleportRailTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class UsefulRailroadsTileEntityTypes {
	
	public static final BlockEntityTypeDeferredRegister TILE_ENTITY_TYPES = BlockEntityTypeDeferredRegister.create(UsefulRailroadsMod.MODID);
	
	public static final RegistryObject<BlockEntityType<TeleportRailTileEntity>> TELEPORT_RAIL = TILE_ENTITY_TYPES.register("teleport_rail", () -> BlockEntityType.Builder.of(TeleportRailTileEntity::new, UsefulRailroadsBlocks.TELEPORT_RAIL.get()));
	
	public static final RegistryObject<BlockEntityType<BufferStopTileEntity>> BUFFER_STOP = TILE_ENTITY_TYPES.register("buffer_stop", () -> BlockEntityType.Builder.of(BufferStopTileEntity::new, UsefulRailroadsBlocks.BUFFER_STOP.get()));
	
	public static void registerMod(IEventBus bus) {
		TILE_ENTITY_TYPES.register(bus);
	}
	
}
