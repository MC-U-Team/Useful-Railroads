package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.util.registry.BlockEntityTypeDeferredRegister;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.blockentity.BufferStopBlockEntity;
import info.u_team.useful_railroads.blockentity.TeleportRailBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class UsefulRailroadsBlockEntityTypes {
	
	public static final BlockEntityTypeDeferredRegister BLOCK_ENTITY_TYPES = BlockEntityTypeDeferredRegister.create(UsefulRailroadsMod.MODID);
	
	public static final RegistryObject<BlockEntityType<TeleportRailBlockEntity>> TELEPORT_RAIL = BLOCK_ENTITY_TYPES.register("teleport_rail", () -> BlockEntityType.Builder.of(TeleportRailBlockEntity::new, UsefulRailroadsBlocks.TELEPORT_RAIL.get()));
	public static final RegistryObject<BlockEntityType<BufferStopBlockEntity>> BUFFER_STOP = BLOCK_ENTITY_TYPES.register("buffer_stop", () -> BlockEntityType.Builder.of(BufferStopBlockEntity::new, UsefulRailroadsBlocks.BUFFER_STOP.get()));
	
	public static void registerMod(IEventBus bus) {
		BLOCK_ENTITY_TYPES.register(bus);
	}
	
}
