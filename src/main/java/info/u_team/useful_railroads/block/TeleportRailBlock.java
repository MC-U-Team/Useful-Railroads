package info.u_team.useful_railroads.block;

import info.u_team.useful_railroads.init.UsefulRailroadsTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TeleportRailBlock extends CustomTileEntityPoweredRailBlock {
	
	public TeleportRailBlock(String name) {
		super(name, () -> UsefulRailroadsTileEntities.TELEPORT_RAIL);
	}
	
	@Override
	public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		super.onMinecartPass(state, world, pos, cart);
	}
	
}
