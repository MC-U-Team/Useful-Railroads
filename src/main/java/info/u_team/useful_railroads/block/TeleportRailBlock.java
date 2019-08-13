package info.u_team.useful_railroads.block;

import info.u_team.useful_railroads.init.UsefulRailroadsTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class TeleportRailBlock extends CustomTileEntityPoweredRailBlock {
	
	public TeleportRailBlock(String name) {
		super(name, () -> UsefulRailroadsTileEntities.TELEPORT_RAIL);
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		return openContainer(world, pos, player, true);
	}
	
	@Override
	public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		super.onMinecartPass(state, world, pos, cart);
	}
	
}
