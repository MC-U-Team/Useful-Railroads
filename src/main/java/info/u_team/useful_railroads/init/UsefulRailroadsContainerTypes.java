package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.containertype.UContainerType;
import info.u_team.u_team_core.util.registry.BaseRegistryUtil;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.container.*;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.MOD)
public class UsefulRailroadsContainerTypes {
	
	public static final ContainerType<TeleportRailContainer> TELEPORT_RAIL = new UContainerType<>("teleport_rail", TeleportRailContainer::new);
	
	public static final ContainerType<TrackBuilderContainer> TRACK_BUILDER = new UContainerType<>("track_builder", TrackBuilderContainer::new);
	
	@SubscribeEvent
	public static void register(Register<ContainerType<?>> event) {
		BaseRegistryUtil.getAllGenericRegistryEntriesAndApplyNames(UsefulRailroadsMod.MODID, ContainerType.class).forEach(event.getRegistry()::register);
	}
	
}
