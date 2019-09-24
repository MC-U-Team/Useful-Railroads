package info.u_team.useful_railroads.init;

import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.network.CycleTrackBuilderMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.MOD)
public class UsefulRailroadsNetwork {
	
	public static final String PROTOCOL = "1.14.4-1";
	
	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(UsefulRailroadsMod.MODID, "network"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);
	
	@SubscribeEvent
	public static void register(FMLCommonSetupEvent event) {
		NETWORK.registerMessage(0, CycleTrackBuilderMessage.class, CycleTrackBuilderMessage::encode, CycleTrackBuilderMessage::decode, CycleTrackBuilderMessage.Handler::handle);
	}
}
