package info.u_team.useful_railroads.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;

public class Location implements INBTSerializable<CompoundTag> {
	
	public static Location getOrigin() {
		return new Location(Level.OVERWORLD, BlockPos.ZERO);
	}
	
	private ResourceKey<Level> resourceKey;
	private BlockPos pos;
	
	public Location(ResourceKey<Level> dimensionType, BlockPos pos) {
		resourceKey = dimensionType;
		this.pos = pos;
	}
	
	public ResourceKey<Level> getResourceKey() {
		return resourceKey;
	}
	
	public void setResourceKey(ResourceKey<Level> dimensionType) {
		resourceKey = dimensionType;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public void setPos(BlockPos pos) {
		this.pos = pos;
	}
	
	public void serialize(FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(resourceKey.location());
		buffer.writeBlockPos(pos);
	}
	
	public void deserialize(FriendlyByteBuf buffer) {
		resourceKey = ResourceKey.create(Registries.DIMENSION, buffer.readResourceLocation());
		if (resourceKey == null) {
			resourceKey = Level.OVERWORLD;
		}
		pos = buffer.readBlockPos();
	}
	
	@Override
	public CompoundTag serializeNBT() {
		final CompoundTag compound = new CompoundTag();
		compound.putString("dimension", resourceKey.location().toString());
		compound.putInt("x", pos.getX());
		compound.putInt("y", pos.getY());
		compound.putInt("z", pos.getZ());
		return compound;
	}
	
	@Override
	public void deserializeNBT(CompoundTag compound) {
		final ResourceLocation dimensionLocation = ResourceLocation.tryParse(compound.getString("dimension"));
		if (dimensionLocation != null) {
			resourceKey = ResourceKey.create(Registries.DIMENSION, dimensionLocation);
		}
		if (resourceKey == null) {
			resourceKey = Level.OVERWORLD;
		}
		pos = new BlockPos(compound.getInt("x"), compound.getInt("y"), compound.getInt("z"));
	}
}
