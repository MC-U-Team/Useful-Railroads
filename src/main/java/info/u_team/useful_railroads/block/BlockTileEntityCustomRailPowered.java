package info.u_team.useful_railroads.block;

import java.util.function.Supplier;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.*;
import net.minecraft.world.IBlockReader;

public abstract class BlockTileEntityCustomRailPowered extends BlockCustomRailPowered {
	
	protected final Supplier<TileEntityType<?>> tileEntityType;
	
	public BlockTileEntityCustomRailPowered(String name, Supplier<TileEntityType<?>> tileEntityType) {
		super(name);
		this.tileEntityType = tileEntityType;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return tileEntityType.get().create();
	}
}
