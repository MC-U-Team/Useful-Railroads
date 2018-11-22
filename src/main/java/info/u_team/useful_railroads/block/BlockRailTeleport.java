package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.tileentity.UTileEntityProvider;
import info.u_team.useful_railroads.UsefulRailroadsConstants;
import info.u_team.useful_railroads.tilentity.TileEntityRailTeleport;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public class BlockRailTeleport extends BlockTileEntityCustomRailPowered {
	
	public BlockRailTeleport(String name) {
		super(name, new UTileEntityProvider(new ResourceLocation(UsefulRailroadsConstants.MODID, name + "_tile"), TileEntityRailTeleport.class));
	}
	
	@Override
	public void onMinecartPass(World world, EntityMinecart cart, BlockPos pos) {
		cart.motionX = 0;
		cart.motionY = 0;
		cart.motionZ = 0;
		
		TileEntity tile = world.getTileEntity(pos);
		if (!(tile instanceof TileEntityRailTeleport)) {
			return;
		}
		TileEntityRailTeleport tileentity = (TileEntityRailTeleport) tile;
		
		int dimension = tileentity.getDimension();
		BlockPos teleport_pos = tileentity.getTeleportPos();
		
		
	}
	
	@Override
	public boolean canMakeSlopes(IBlockAccess world, BlockPos pos) {
		return false;
	}
	
}
