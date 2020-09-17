package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.util.registry.TileEntityTypeDeferredRegister;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.tileentity.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;

public class UsefulRailroadsTileEntityTypes {
	
	public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = TileEntityTypeDeferredRegister.create(UsefulRailroadsMod.MODID);
	
	public static final RegistryObject<TileEntityType<TeleportRailTileEntity>> TELEPORT_RAIL = TILE_ENTITY_TYPES.register("teleport_rail", () -> TileEntityType.Builder.create(TeleportRailTileEntity::new, UsefulRailroadsBlocks.TELEPORT_RAIL.get()));
	
	public static final RegistryObject<TileEntityType<BufferStopTileEntity>> BUFFER_STOP = TILE_ENTITY_TYPES.register("buffer_stop", () -> TileEntityType.Builder.create(BufferStopTileEntity::new, UsefulRailroadsBlocks.BUFFER_STOP.get()));
	
	public static void registerMod(IEventBus bus) {
		TILE_ENTITY_TYPES.register(bus);
	}
	
}
