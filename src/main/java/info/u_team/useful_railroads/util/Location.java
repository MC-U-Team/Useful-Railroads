package info.u_team.useful_railroads.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.INBTSerializable;

public class Location implements INBTSerializable<CompoundNBT> {
	
	public static Location getOrigin() {
		return new Location(DimensionType.OVERWORLD, BlockPos.ZERO);
	}
	
	private DimensionType dimensionType;
	private BlockPos pos;
	
	public Location(DimensionType dimensionType, BlockPos pos) {
		this.dimensionType = dimensionType;
		this.pos = pos;
	}
	
	public DimensionType getDimensionType() {
		return dimensionType;
	}
	
	public void setDimensionType(DimensionType dimensionType) {
		this.dimensionType = dimensionType;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public void setPos(BlockPos pos) {
		this.pos = pos;
	}
	
	public void serialize(PacketBuffer buffer) {
		buffer.writeResourceLocation(dimensionType.getRegistryName());
		buffer.writeBlockPos(pos);
	}
	
	public void deserialize(PacketBuffer buffer) {
		dimensionType = DimensionType.byName(buffer.readResourceLocation());
		if (dimensionType == null) {
			dimensionType = DimensionType.OVERWORLD;
		}
		pos = buffer.readBlockPos();
	}
	
	@Override
	public CompoundNBT serializeNBT() {
		final CompoundNBT compound = new CompoundNBT();
		compound.putString("dimension", dimensionType.getRegistryName().toString());
		compound.putInt("x", pos.getX());
		compound.putInt("y", pos.getY());
		compound.putInt("z", pos.getZ());
		return compound;
	}
	
	@Override
	public void deserializeNBT(CompoundNBT compound) {
		final ResourceLocation dimensionLocation = ResourceLocation.tryCreate(compound.getString("dimension"));
		if (dimensionLocation != null) {
			dimensionType = DimensionType.byName(dimensionLocation);
		}
		if (dimensionType == null) {
			dimensionType = DimensionType.OVERWORLD;
		}
		pos = new BlockPos(compound.getInt("x"), compound.getInt("y"), compound.getInt("z"));
	}
}
