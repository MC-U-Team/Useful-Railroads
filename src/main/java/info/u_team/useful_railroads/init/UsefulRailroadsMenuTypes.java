package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.registry.CommonRegister;
import info.u_team.u_team_core.api.registry.RegistryEntry;
import info.u_team.u_team_core.menutype.UMenuType;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.menu.TeleportRailMenu;
import info.u_team.useful_railroads.menu.TrackBuilderMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class UsefulRailroadsMenuTypes {
	
	public static final CommonRegister<MenuType<?>> CONTAINER_TYPES = CommonRegister.create(Registries.MENU, UsefulRailroadsMod.MODID);
	
	public static final RegistryEntry<MenuType<TeleportRailMenu>> TELEPORT_RAIL = CONTAINER_TYPES.register("teleport_rail", () -> new UMenuType<>(TeleportRailMenu::new));
	public static final RegistryEntry<MenuType<TrackBuilderMenu>> TRACK_BUILDER = CONTAINER_TYPES.register("track_builder", () -> new UMenuType<>(TrackBuilderMenu::new));
	
	static void register() {
		CONTAINER_TYPES.register();
	}
	
}
