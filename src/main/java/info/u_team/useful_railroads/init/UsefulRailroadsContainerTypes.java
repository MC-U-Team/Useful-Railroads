package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.containertype.UContainerType;
import info.u_team.u_team_core.util.registry.CommonDeferredRegister;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.container.*;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class UsefulRailroadsContainerTypes {
	
	public static final CommonDeferredRegister<ContainerType<?>> CONTAINER_TYPES = CommonDeferredRegister.create(ForgeRegistries.CONTAINERS, UsefulRailroadsMod.MODID);
	
	public static final RegistryObject<ContainerType<TeleportRailContainer>> TELEPORT_RAIL = CONTAINER_TYPES.register("teleport_rail", () -> new UContainerType<>(TeleportRailContainer::new));
	
	public static final RegistryObject<ContainerType<TrackBuilderContainer>> TRACK_BUILDER = CONTAINER_TYPES.register("track_builder", () -> new UContainerType<>(TrackBuilderContainer::new));
	
	public static void registerMod(IEventBus bus) {
		CONTAINER_TYPES.register(bus);
	}
	
}
