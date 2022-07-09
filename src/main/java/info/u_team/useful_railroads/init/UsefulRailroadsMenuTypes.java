package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.menutype.UMenuType;
import info.u_team.u_team_core.util.registry.CommonDeferredRegister;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.menu.TeleportRailMenu;
import info.u_team.useful_railroads.menu.TrackBuilderMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class UsefulRailroadsMenuTypes {
	
	public static final CommonDeferredRegister<MenuType<?>> CONTAINER_TYPES = CommonDeferredRegister.create(ForgeRegistries.CONTAINERS, UsefulRailroadsMod.MODID);
	
	public static final RegistryObject<MenuType<TeleportRailMenu>> TELEPORT_RAIL = CONTAINER_TYPES.register("teleport_rail", () -> new UMenuType<>(TeleportRailMenu::new));
	public static final RegistryObject<MenuType<TrackBuilderMenu>> TRACK_BUILDER = CONTAINER_TYPES.register("track_builder", () -> new UMenuType<>(TrackBuilderMenu::new));
	
	public static void registerMod(IEventBus bus) {
		CONTAINER_TYPES.register(bus);
	}
	
}
