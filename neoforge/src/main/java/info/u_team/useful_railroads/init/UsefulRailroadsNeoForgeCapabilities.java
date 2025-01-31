package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.util.CastUtil;
import info.u_team.u_team_core.util.registry.BusRegister;
import info.u_team.useful_railroads.blockentity.NeoForgeBufferStopBlockEntity;
import info.u_team.useful_railroads.blockentity.NeoForgeTeleportRailBlockEntity;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class UsefulRailroadsNeoForgeCapabilities {
	
	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, UsefulRailroadsBlockEntityTypes.TELEPORT_RAIL.get(), (blockEntity, context) -> {
			if (context != Direction.UP) {
				return CastUtil.assertCast(blockEntity, NeoForgeTeleportRailBlockEntity.class).getFuelSlotWrapper();
			}
			return null;
		});
		
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, UsefulRailroadsBlockEntityTypes.BUFFER_STOP.get(), (blockEntity, context) -> {
			if (context == Direction.DOWN || context == null) {
				return CastUtil.assertCast(blockEntity, NeoForgeBufferStopBlockEntity.class).getMinecartSlotsWrapper();
			}
			return null;
		});
	}
	
	static void register() {
		BusRegister.registerMod(bus -> bus.addListener(UsefulRailroadsNeoForgeCapabilities::registerCapabilities));
	}
	
}
