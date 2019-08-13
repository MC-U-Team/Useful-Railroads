package info.u_team.useful_railroads.block;

import java.util.function.Supplier;

import info.u_team.u_team_core.api.ITileEntityBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class CustomTileEntityPoweredRailBlock extends CustomPoweredRailBlock implements ITileEntityBlock {
	
	protected final Supplier<TileEntityType<?>> tileEntityType;
	
	public CustomTileEntityPoweredRailBlock(String name, Supplier<TileEntityType<?>> tileEntityType) {
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
	
	@Override
	public TileEntityType<?> getTileEntityType(IBlockReader world, BlockPos pos) {
		return tileEntityType.get();
	}
	
}
