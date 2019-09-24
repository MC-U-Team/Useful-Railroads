package info.u_team.useful_railroads.network;

import java.util.Optional;
import java.util.function.Supplier;

import info.u_team.useful_railroads.container.TrackBuilderContainer;
import info.u_team.useful_railroads.inventory.TrackBuilderInventoryWrapper;
import info.u_team.useful_railroads.util.TrackBuilderMode;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class CycleTrackBuilderMessage {
	
	public static void encode(CycleTrackBuilderMessage message, PacketBuffer sendBuffer) {
	}
	
	public static CycleTrackBuilderMessage decode(PacketBuffer sendBuffer) {
		return new CycleTrackBuilderMessage();
	}
	
	public static class Handler {
		
		public static void handle(CycleTrackBuilderMessage message, Supplier<Context> contextSupplier) {
			final Context context = contextSupplier.get();
			context.enqueueWork(() -> getTrackBuilderContainer(context).ifPresent(container -> {
				final TrackBuilderInventoryWrapper wrapper = container.getWrapper();
				wrapper.setMode(TrackBuilderMode.cycle(wrapper.getMode()));
				wrapper.writeItemStack();
			}));
			context.setPacketHandled(true);
		}
		
		private static final Optional<TrackBuilderContainer> getTrackBuilderContainer(Context context) {
			final Container container = context.getSender().openContainer;
			if (!(container instanceof TrackBuilderContainer)) {
				return Optional.empty();
			}
			return Optional.of((TrackBuilderContainer) container);
		}
	}
}
