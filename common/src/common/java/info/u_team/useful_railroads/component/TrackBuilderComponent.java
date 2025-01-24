package info.u_team.useful_railroads.component;

import java.util.List;
import java.util.Objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import info.u_team.useful_railroads.util.TrackBuilderMode;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public class TrackBuilderComponent {
	
	public static final Codec<TrackBuilderComponent> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group( //
				Items.CODEC.fieldOf("items").forGetter(component -> component.items), //
				Codec.INT.orElse(0).fieldOf("fuel").forGetter(component -> component.fuel), //
				TrackBuilderMode.CODEC.orElse(TrackBuilderMode.MODE_NOAIR).fieldOf("mode").forGetter(component -> component.mode)) //
				.apply(instance, TrackBuilderComponent::new);
	});
	
	public static final StreamCodec<RegistryFriendlyByteBuf, TrackBuilderComponent> STREAM_CODEC = StreamCodec.composite( //
			Items.STREAM_CODEC, component -> component.items, //
			ByteBufCodecs.VAR_INT, component -> component.fuel, //
			TrackBuilderMode.STREAM_CODEC, component -> component.mode, //
			TrackBuilderComponent::new);
	
	public static final TrackBuilderComponent EMPTY = new TrackBuilderComponent(Items.EMPTY, 0, TrackBuilderMode.MODE_NOAIR);
	
	private final Items items;
	private final int fuel;
	private final TrackBuilderMode mode;
	
	private TrackBuilderComponent(Items items, int fuel, TrackBuilderMode mode) {
		this.items = items;
		this.fuel = fuel;
		this.mode = mode;
	}
	
	public static TrackBuilderComponent of(List<ItemStack> rails, List<ItemStack> groundBlocks, List<ItemStack> tunnelBlocks, List<ItemStack> redstoneTorches, List<ItemStack> torches, int fuel, TrackBuilderMode mode) {
		final Items items = new Items(ItemContainerContents.fromItems(rails), ItemContainerContents.fromItems(groundBlocks), ItemContainerContents.fromItems(tunnelBlocks), ItemContainerContents.fromItems(redstoneTorches), ItemContainerContents.fromItems(torches));
		return new TrackBuilderComponent(items, fuel, mode);
	}
	
	public int getFuel() {
		return fuel;
	}
	
	public TrackBuilderMode getMode() {
		return mode;
	}
	
	public ItemContainerContents getRails() {
		return items.rails;
	}
	
	public ItemContainerContents getGroundBlocks() {
		return items.groundBlocks;
	}
	
	public ItemContainerContents getTunnelBlocks() {
		return items.tunnelBlocks;
	}
	
	public ItemContainerContents getRedstoneTorches() {
		return items.redstoneTorches;
	}
	
	public ItemContainerContents getTorches() {
		return items.torches;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(fuel, items, mode);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TrackBuilderComponent other = (TrackBuilderComponent) obj;
		return fuel == other.fuel && Objects.equals(items, other.items) && mode == other.mode;
	}
	
	private record Items(ItemContainerContents rails, ItemContainerContents groundBlocks, ItemContainerContents tunnelBlocks, ItemContainerContents redstoneTorches, ItemContainerContents torches) {
		
		private static final Codec<Items> CODEC = RecordCodecBuilder.create(instance -> {
			return instance.group( //
					ItemContainerContents.CODEC.fieldOf("rails").forGetter(Items::rails), //
					ItemContainerContents.CODEC.fieldOf("ground_blocks").forGetter(Items::groundBlocks), //
					ItemContainerContents.CODEC.fieldOf("tunnel_blocks").forGetter(Items::tunnelBlocks), //
					ItemContainerContents.CODEC.fieldOf("redstone_torches").forGetter(Items::redstoneTorches), //
					ItemContainerContents.CODEC.fieldOf("torches").forGetter(Items::torches)) //
					.apply(instance, Items::new);
		});
		
		private static final StreamCodec<RegistryFriendlyByteBuf, Items> STREAM_CODEC = StreamCodec.composite( //
				ItemContainerContents.STREAM_CODEC, Items::rails, //
				ItemContainerContents.STREAM_CODEC, Items::groundBlocks, //
				ItemContainerContents.STREAM_CODEC, Items::tunnelBlocks, //
				ItemContainerContents.STREAM_CODEC, Items::redstoneTorches, //
				ItemContainerContents.STREAM_CODEC, Items::torches, //
				Items::new);
		
		private static final Items EMPTY = new Items(ItemContainerContents.EMPTY, ItemContainerContents.EMPTY, ItemContainerContents.EMPTY, ItemContainerContents.EMPTY, ItemContainerContents.EMPTY);
		
	}
	
}
