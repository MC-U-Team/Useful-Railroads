package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.menutype.UMenuType;
import info.u_team.u_team_core.util.registry.CommonDeferredRegister;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.container.TeleportRailContainer;
import info.u_team.useful_railroads.container.TrackBuilderContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class UsefulRailroadsContainerTypes {
	
	public static final CommonDeferredRegister<MenuType<?>> CONTAINER_TYPES = CommonDeferredRegister.create(ForgeRegistries.CONTAINERS, UsefulRailroadsMod.MODID);
	
	public static final RegistryObject<MenuType<TeleportRailContainer>> TELEPORT_RAIL = CONTAINER_TYPES.register("teleport_rail", () -> new UMenuType<>(TeleportRailContainer::new));
	
	public static final RegistryObject<MenuType<TrackBuilderContainer>> TRACK_BUILDER = CONTAINER_TYPES.register("track_builder", () -> new UMenuType<>(TrackBuilderContainer::new));
	
	public static void registerMod(IEventBus bus) {
		CONTAINER_TYPES.register(bus);
	}
	
}
