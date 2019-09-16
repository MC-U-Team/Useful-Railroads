package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.tileentitytype.UTileEntityType.UBuilder;
import info.u_team.u_team_core.util.registry.BaseRegistryUtil;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.tileentity.TeleportRailTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.MOD)
public class UsefulRailroadsTileEntityTypes {
	
	public static final TileEntityType<TeleportRailTileEntity> TELEPORT_RAIL = UBuilder.create("teleport_rail", TeleportRailTileEntity::new, UsefulRailroadsBlocks.TELEPORT_RAIL).build();
	
	@SubscribeEvent
	public static void register(Register<TileEntityType<?>> event) {
		BaseRegistryUtil.getAllGenericRegistryEntriesAndApplyNames(UsefulRailroadsMod.MODID, TileEntityType.class).forEach(event.getRegistry()::register);
	}
	
}
