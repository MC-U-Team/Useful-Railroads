package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.registry.BlockEntityTypeRegister;
import info.u_team.u_team_core.api.registry.RegistryEntry;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.blockentity.BufferStopBlockEntity;
import info.u_team.useful_railroads.blockentity.TeleportRailBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class UsefulRailroadsBlockEntityTypes {
	
	public static final BlockEntityTypeRegister BLOCK_ENTITY_TYPES = BlockEntityTypeRegister.create(UsefulRailroadsMod.MODID);
	
	public static final RegistryEntry<BlockEntityType<TeleportRailBlockEntity>> TELEPORT_RAIL = BLOCK_ENTITY_TYPES.register("teleport_rail", () -> BlockEntityType.Builder.of(TeleportRailBlockEntity::new, UsefulRailroadsBlocks.TELEPORT_RAIL.get()));
	public static final RegistryEntry<BlockEntityType<BufferStopBlockEntity>> BUFFER_STOP = BLOCK_ENTITY_TYPES.register("buffer_stop", () -> BlockEntityType.Builder.of(BufferStopBlockEntity::new, UsefulRailroadsBlocks.BUFFER_STOP.get()));
	
	static void register() {
		BLOCK_ENTITY_TYPES.register();
	}
	
}
