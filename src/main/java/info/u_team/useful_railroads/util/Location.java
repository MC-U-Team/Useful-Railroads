package info.u_team.useful_railroads.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public class Location implements INBTSerializable<CompoundNBT> {
	
	public static Location getOrigin() {
		return new Location(World.OVERWORLD, BlockPos.ZERO);
	}
	
	private RegistryKey<World> registryKey;
	private BlockPos pos;
	
	public Location(RegistryKey<World> dimensionType, BlockPos pos) {
		this.registryKey = dimensionType;
		this.pos = pos;
	}
	
	public RegistryKey<World> getRegistryKey() {
		return registryKey;
	}
	
	public void setRegistryKey(RegistryKey<World> dimensionType) {
		this.registryKey = dimensionType;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public void setPos(BlockPos pos) {
		this.pos = pos;
	}
	
	public void serialize(PacketBuffer buffer) {
		buffer.writeResourceLocation(registryKey.getLocation());
		buffer.writeBlockPos(pos);
	}
	
	public void deserialize(PacketBuffer buffer) {
		registryKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, buffer.readResourceLocation());
		if (registryKey == null) {
			registryKey = World.OVERWORLD;
		}
		pos = buffer.readBlockPos();
	}
	
	@Override
	public CompoundNBT serializeNBT() {
		final CompoundNBT compound = new CompoundNBT();
		compound.putString("dimension", registryKey.getLocation().toString());
		compound.putInt("x", pos.getX());
		compound.putInt("y", pos.getY());
		compound.putInt("z", pos.getZ());
		return compound;
	}
	
	@Override
	public void deserializeNBT(CompoundNBT compound) {
		final ResourceLocation dimensionLocation = ResourceLocation.tryCreate(compound.getString("dimension"));
		if (dimensionLocation != null) {
			registryKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, dimensionLocation);
		}
		if (registryKey == null) {
			registryKey = World.OVERWORLD;
		}
		pos = new BlockPos(compound.getInt("x"), compound.getInt("y"), compound.getInt("z"));
	}
}
