package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.tileentity.UTileEntityProvider;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockTileEntityCustomRailPowered extends BlockCustomRailPowered implements ITileEntityProvider {
	
	private UTileEntityProvider provider;
	
	public BlockTileEntityCustomRailPowered(String name, UTileEntityProvider provider) {
		super(name);
		this.provider = provider;
		this.hasTileEntity = true;
	}
	
	@Override
	public boolean eventReceived(IBlockState state, World world, BlockPos pos, int id, int param) {
		TileEntity tileentity = world.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return provider.create(world, meta);
	}
	
}
